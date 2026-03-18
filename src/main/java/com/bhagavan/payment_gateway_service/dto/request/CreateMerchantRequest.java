package com.bhagavan.payment_gateway_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateMerchantRequest {

    @NotBlank(message = "Merchant code is required")
    private String merchantCode;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    // ✅ GETTERS
    public String getMerchantCode() {
        return merchantCode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // ✅ SETTERS
    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}