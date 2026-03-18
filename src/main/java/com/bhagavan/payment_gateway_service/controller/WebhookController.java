package com.bhagavan.payment_gateway_service.controller;

import com.bhagavan.payment_gateway_service.dto.response.ApiResponse;
import com.bhagavan.payment_gateway_service.dto.response.WebhookEventResponse;
import com.bhagavan.payment_gateway_service.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/retry/{webhookEventId}")
    public ResponseEntity<ApiResponse<WebhookEventResponse>> retryWebhook(
            @PathVariable Long webhookEventId) {

        WebhookEventResponse response = webhookService.retryWebhook(webhookEventId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Webhook retried successfully", response)
        );
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<ApiResponse<List<WebhookEventResponse>>> getWebhooksByPaymentId(
            @PathVariable Long paymentId) {

        List<WebhookEventResponse> response = webhookService.getWebhooksByPaymentId(paymentId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Webhook events fetched successfully", response)
        );
    }
}