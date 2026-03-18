package com.bhagavan.payment_gateway_service.service.impl;

import com.bhagavan.payment_gateway_service.dto.request.CreatePaymentRequest;
import com.bhagavan.payment_gateway_service.dto.request.UpdatePaymentStatusRequest;
import com.bhagavan.payment_gateway_service.dto.response.PaymentResponse;
import com.bhagavan.payment_gateway_service.entity.Merchant;
import com.bhagavan.payment_gateway_service.entity.Payment;
import com.bhagavan.payment_gateway_service.enums.PaymentStatus;
import com.bhagavan.payment_gateway_service.exception.InvalidPaymentStateException;
import com.bhagavan.payment_gateway_service.exception.ResourceNotFoundException;
import com.bhagavan.payment_gateway_service.repository.MerchantRepository;
import com.bhagavan.payment_gateway_service.repository.PaymentRepository;
import com.bhagavan.payment_gateway_service.service.PaymentService;
import com.bhagavan.payment_gateway_service.util.PaymentReferenceGenerator;
import org.springframework.stereotype.Service;
import com.bhagavan.payment_gateway_service.service.LedgerService;
import com.bhagavan.payment_gateway_service.service.WebhookService;
import com.bhagavan.payment_gateway_service.service.AuditLogService;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MerchantRepository merchantRepository;
    private final LedgerService ledgerService;
    private final WebhookService webhookService;
    private final AuditLogService auditLogService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              MerchantRepository merchantRepository,
                              LedgerService ledgerService,
                              WebhookService webhookService,
                              AuditLogService auditLogService) {
        this.paymentRepository = paymentRepository;
        this.merchantRepository = merchantRepository;
        this.ledgerService = ledgerService;
        this.webhookService = webhookService;
        this.auditLogService = auditLogService;
    }

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Optional<Payment> existingPayment = paymentRepository.findByIdempotencyKey(request.getIdempotencyKey());

        if (existingPayment.isPresent()) {
            return mapToResponse(existingPayment.get());
        }

        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + request.getMerchantId()));

        Payment payment = new Payment();
        payment.setPaymentRef(PaymentReferenceGenerator.generatePaymentRef());
        payment.setMerchant(merchant);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency().toUpperCase(Locale.ROOT));
        payment.setStatus(PaymentStatus.CREATED);
        payment.setCustomerRef(request.getCustomerRef());
        payment.setIdempotencyKey(request.getIdempotencyKey());
        payment.setDescription(request.getDescription());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        auditLogService.logAction(
                "PAYMENT",
                savedPayment.getId(),
                "CREATE",
                "Payment created with reference " + savedPayment.getPaymentRef()
        );

        return mapToResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        PaymentStatus newStatus;
        try {
            newStatus = PaymentStatus.valueOf(request.getStatus().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new InvalidPaymentStateException("Invalid payment status: " + request.getStatus());
        }

        validateStatusTransition(payment.getStatus(), newStatus);

        payment.setStatus(newStatus);
        payment.setUpdatedAt(LocalDateTime.now());

        Payment updatedPayment = paymentRepository.save(payment);

        if (newStatus == PaymentStatus.SUCCESS) {
            ledgerService.createEntriesForSuccessfulPayment(updatedPayment.getId());
            webhookService.createPaymentSuccessWebhook(updatedPayment.getId());
        }
        auditLogService.logAction(
                "PAYMENT",
                updatedPayment.getId(),
                "STATUS_UPDATE",
                "Payment status changed to " + updatedPayment.getStatus()
        );
        return mapToResponse(updatedPayment);
    }

    private void validateStatusTransition(PaymentStatus currentStatus, PaymentStatus newStatus) {
        if (currentStatus == PaymentStatus.SUCCESS || currentStatus == PaymentStatus.FAILED) {
            throw new InvalidPaymentStateException(
                    "Payment status cannot change once it is " + currentStatus
            );
        }

        if (currentStatus == newStatus) {
            throw new InvalidPaymentStateException(
                    "Payment is already in status " + currentStatus
            );
        }
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setPaymentRef(payment.getPaymentRef());
        response.setMerchantId(payment.getMerchant().getId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setStatus(payment.getStatus());
        response.setCustomerRef(payment.getCustomerRef());
        response.setIdempotencyKey(payment.getIdempotencyKey());
        response.setDescription(payment.getDescription());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }
}