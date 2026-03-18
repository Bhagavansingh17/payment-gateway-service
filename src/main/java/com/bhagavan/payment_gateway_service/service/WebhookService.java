package com.bhagavan.payment_gateway_service.service;

import com.bhagavan.payment_gateway_service.dto.response.WebhookEventResponse;

import java.util.List;

public interface WebhookService {
    void createPaymentSuccessWebhook(Long paymentId);
    WebhookEventResponse retryWebhook(Long webhookEventId);
    List<WebhookEventResponse> getWebhooksByPaymentId(Long paymentId);
}