package com.bhagavan.payment_gateway_service.service;

import com.bhagavan.payment_gateway_service.dto.request.CreateMerchantRequest;
import com.bhagavan.payment_gateway_service.dto.response.MerchantResponse;

public interface MerchantService {
    MerchantResponse createMerchant(CreateMerchantRequest request);
    MerchantResponse getMerchantById(Long id);
}