package com.bhagavan.payment_gateway_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdatePaymentStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;

    public UpdatePaymentStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}