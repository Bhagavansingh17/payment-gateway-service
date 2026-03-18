package com.bhagavan.payment_gateway_service.service;

import com.bhagavan.payment_gateway_service.dto.response.LedgerEntryResponse;

import java.util.List;

public interface LedgerService {
    void createEntriesForSuccessfulPayment(Long paymentId);
    List<LedgerEntryResponse> getEntriesByPaymentId(Long paymentId);
}