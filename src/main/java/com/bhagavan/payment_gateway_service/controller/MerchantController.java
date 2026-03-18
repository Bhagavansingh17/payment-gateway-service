package com.bhagavan.payment_gateway_service.controller;

import com.bhagavan.payment_gateway_service.dto.request.CreateMerchantRequest;
import com.bhagavan.payment_gateway_service.dto.response.ApiResponse;
import com.bhagavan.payment_gateway_service.dto.response.MerchantResponse;
import com.bhagavan.payment_gateway_service.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MerchantResponse>> createMerchant(
            @Valid @RequestBody CreateMerchantRequest request) {

        MerchantResponse response = merchantService.createMerchant(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Merchant created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MerchantResponse>> getMerchantById(@PathVariable Long id) {
        MerchantResponse response = merchantService.getMerchantById(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Merchant fetched successfully", response));
    }
}