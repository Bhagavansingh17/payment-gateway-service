package com.bhagavan.payment_gateway_service.service.impl;

import com.bhagavan.payment_gateway_service.dto.response.LedgerEntryResponse;
import com.bhagavan.payment_gateway_service.entity.LedgerEntry;
import com.bhagavan.payment_gateway_service.entity.Payment;
import com.bhagavan.payment_gateway_service.enums.LedgerEntryType;
import com.bhagavan.payment_gateway_service.exception.ResourceNotFoundException;
import com.bhagavan.payment_gateway_service.repository.LedgerEntryRepository;
import com.bhagavan.payment_gateway_service.repository.PaymentRepository;
import com.bhagavan.payment_gateway_service.service.LedgerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LedgerServiceImpl implements LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;
    private final PaymentRepository paymentRepository;

    public LedgerServiceImpl(LedgerEntryRepository ledgerEntryRepository,
                             PaymentRepository paymentRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void createEntriesForSuccessfulPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        List<LedgerEntry> existingEntries = ledgerEntryRepository.findByPaymentId(paymentId);
        if (!existingEntries.isEmpty()) {
            return;
        }

        LedgerEntry debitEntry = new LedgerEntry();
        debitEntry.setPayment(payment);
        debitEntry.setEntryType(LedgerEntryType.DEBIT);
        debitEntry.setAmount(payment.getAmount());
        debitEntry.setCurrency(payment.getCurrency());
        debitEntry.setRemarks("Amount debited from customer for payment " + payment.getPaymentRef());
        debitEntry.setCreatedAt(LocalDateTime.now());

        LedgerEntry creditEntry = new LedgerEntry();
        creditEntry.setPayment(payment);
        creditEntry.setEntryType(LedgerEntryType.CREDIT);
        creditEntry.setAmount(payment.getAmount());
        creditEntry.setCurrency(payment.getCurrency());
        creditEntry.setRemarks("Amount credited to merchant for payment " + payment.getPaymentRef());
        creditEntry.setCreatedAt(LocalDateTime.now());

        ledgerEntryRepository.save(debitEntry);
        ledgerEntryRepository.save(creditEntry);
    }

    @Override
    public List<LedgerEntryResponse> getEntriesByPaymentId(Long paymentId) {
        List<LedgerEntry> entries = ledgerEntryRepository.findByPaymentId(paymentId);
        List<LedgerEntryResponse> responseList = new ArrayList<>();

        for (LedgerEntry entry : entries) {
            LedgerEntryResponse response = new LedgerEntryResponse();
            response.setId(entry.getId());
            response.setPaymentId(entry.getPayment().getId());
            response.setEntryType(entry.getEntryType());
            response.setAmount(entry.getAmount());
            response.setCurrency(entry.getCurrency());
            response.setRemarks(entry.getRemarks());
            response.setCreatedAt(entry.getCreatedAt());

            responseList.add(response);
        }

        return responseList;
    }
}