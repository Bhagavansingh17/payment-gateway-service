package com.bhagavan.payment_gateway_service.service;

import com.bhagavan.payment_gateway_service.dto.request.CreatePaymentRequest;
import com.bhagavan.payment_gateway_service.dto.request.UpdatePaymentStatusRequest;
import com.bhagavan.payment_gateway_service.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    PaymentResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request);
}