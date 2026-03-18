package com.bhagavan.payment_gateway_service.dto.response;

import com.bhagavan.payment_gateway_service.enums.MerchantStatus;

import java.time.LocalDateTime;

public class MerchantResponse {

    private Long id;
    private String merchantCode;
    private String name;
    private String email;
    private MerchantStatus status;
    private LocalDateTime createdAt;

    public MerchantResponse() {
    }

    public MerchantResponse(Long id, String merchantCode, String name, String email,
                            MerchantStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.merchantCode = merchantCode;
        this.name = name;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MerchantStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(MerchantStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}