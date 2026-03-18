package com.bhagavan.payment_gateway_service.service.impl;

import com.bhagavan.payment_gateway_service.dto.response.WebhookEventResponse;
import com.bhagavan.payment_gateway_service.entity.Payment;
import com.bhagavan.payment_gateway_service.entity.WebhookEvent;
import com.bhagavan.payment_gateway_service.enums.WebhookDeliveryStatus;
import com.bhagavan.payment_gateway_service.exception.ResourceNotFoundException;
import com.bhagavan.payment_gateway_service.repository.PaymentRepository;
import com.bhagavan.payment_gateway_service.repository.WebhookEventRepository;
import com.bhagavan.payment_gateway_service.service.WebhookService;
import org.springframework.stereotype.Service;
import com.bhagavan.payment_gateway_service.service.AuditLogService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebhookServiceImpl implements WebhookService {

    private final WebhookEventRepository webhookEventRepository;
    private final PaymentRepository paymentRepository;
    private final AuditLogService auditLogService;

    public WebhookServiceImpl(WebhookEventRepository webhookEventRepository,
                              PaymentRepository paymentRepository,
                              AuditLogService auditLogService) {
        this.webhookEventRepository = webhookEventRepository;
        this.paymentRepository = paymentRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public void createPaymentSuccessWebhook(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        WebhookEvent webhookEvent = new WebhookEvent();
        webhookEvent.setPayment(payment);
        webhookEvent.setTargetUrl("https://merchant.example.com/webhooks/payment-status");
        webhookEvent.setPayload(buildPayload(payment));
        webhookEvent.setDeliveryStatus(WebhookDeliveryStatus.PENDING);
        webhookEvent.setRetryCount(0);
        webhookEvent.setCreatedAt(LocalDateTime.now());

        WebhookEvent savedEvent = webhookEventRepository.save(webhookEvent);

        auditLogService.logAction(
                "WEBHOOK",
                savedEvent.getId(),
                "CREATE",
                "Webhook event created for payment id " + paymentId
        );

        simulateDelivery(savedEvent);
    }

    @Override
    public WebhookEventResponse retryWebhook(Long webhookEventId) {
        WebhookEvent webhookEvent = webhookEventRepository.findById(webhookEventId)
                .orElseThrow(() -> new ResourceNotFoundException("Webhook event not found with id: " + webhookEventId));

        webhookEvent.setRetryCount(webhookEvent.getRetryCount() + 1);
        webhookEvent.setLastAttemptAt(LocalDateTime.now());

        WebhookEvent savedEvent = webhookEventRepository.save(webhookEvent);

        auditLogService.logAction(
                "WEBHOOK",
                savedEvent.getId(),
                "RETRY",
                "Webhook retried for payment id " + savedEvent.getPayment().getId()
        );

        simulateDelivery(savedEvent);

        return mapToResponse(savedEvent);
    }

    @Override
    public List<WebhookEventResponse> getWebhooksByPaymentId(Long paymentId) {
        List<WebhookEvent> events = webhookEventRepository.findByPaymentId(paymentId);
        List<WebhookEventResponse> responses = new ArrayList<>();

        for (WebhookEvent event : events) {
            responses.add(mapToResponse(event));
        }

        return responses;
    }

    private void simulateDelivery(WebhookEvent webhookEvent) {
        webhookEvent.setLastAttemptAt(LocalDateTime.now());

        if (webhookEvent.getRetryCount() >= 1) {
            webhookEvent.setDeliveryStatus(WebhookDeliveryStatus.SENT);
        } else {
            webhookEvent.setDeliveryStatus(WebhookDeliveryStatus.FAILED);
        }

        webhookEventRepository.save(webhookEvent);
    }

    private String buildPayload(Payment payment) {
        return "{"
                + "\"paymentRef\":\"" + payment.getPaymentRef() + "\","
                + "\"status\":\"" + payment.getStatus() + "\","
                + "\"amount\":" + payment.getAmount() + ","
                + "\"currency\":\"" + payment.getCurrency() + "\""
                + "}";
    }

    private WebhookEventResponse mapToResponse(WebhookEvent event) {
        WebhookEventResponse response = new WebhookEventResponse();
        response.setId(event.getId());
        response.setPaymentId(event.getPayment().getId());
        response.setTargetUrl(event.getTargetUrl());
        response.setPayload(event.getPayload());
        response.setDeliveryStatus(event.getDeliveryStatus());
        response.setRetryCount(event.getRetryCount());
        response.setLastAttemptAt(event.getLastAttemptAt());
        response.setCreatedAt(event.getCreatedAt());
        return response;
    }
}