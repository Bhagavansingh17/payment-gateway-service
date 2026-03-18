package com.bhagavan.payment_gateway_service.controller;

import com.bhagavan.payment_gateway_service.dto.response.ApiResponse;
import com.bhagavan.payment_gateway_service.dto.response.LedgerEntryResponse;
import com.bhagavan.payment_gateway_service.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<ApiResponse<List<LedgerEntryResponse>>> getLedgerEntriesByPaymentId(
            @PathVariable Long paymentId) {

        List<LedgerEntryResponse> response = ledgerService.getEntriesByPaymentId(paymentId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Ledger entries fetched successfully", response)
        );
    }
}