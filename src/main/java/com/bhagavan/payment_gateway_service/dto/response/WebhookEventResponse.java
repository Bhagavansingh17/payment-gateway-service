package com.bhagavan.payment_gateway_service.dto.response;

import com.bhagavan.payment_gateway_service.enums.WebhookDeliveryStatus;

import java.time.LocalDateTime;

public class WebhookEventResponse {

    private Long id;
    private Long paymentId;
    private String targetUrl;
    private String payload;
    private WebhookDeliveryStatus deliveryStatus;
    private Integer retryCount;
    private LocalDateTime lastAttemptAt;
    private LocalDateTime createdAt;

    public WebhookEventResponse() {
    }

    public Long getId() {
        return id;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getPayload() {
        return payload;
    }

    public WebhookDeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public LocalDateTime getLastAttemptAt() {
        return lastAttemptAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setDeliveryStatus(WebhookDeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public void setLastAttemptAt(LocalDateTime lastAttemptAt) {
        this.lastAttemptAt = lastAttemptAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}