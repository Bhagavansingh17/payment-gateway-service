package com.bhagavan.payment_gateway_service.controller;

import com.bhagavan.payment_gateway_service.dto.request.CreatePaymentRequest;
import com.bhagavan.payment_gateway_service.dto.request.UpdatePaymentStatusRequest;
import com.bhagavan.payment_gateway_service.dto.response.ApiResponse;
import com.bhagavan.payment_gateway_service.dto.response.PaymentResponse;
import com.bhagavan.payment_gateway_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentResponse response = paymentService.createPayment(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Payment created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long id) {
        PaymentResponse response = paymentService.getPaymentById(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Payment fetched successfully", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePaymentStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentStatusRequest request) {

        PaymentResponse response = paymentService.updatePaymentStatus(id, request);

        return ResponseEntity.ok(new ApiResponse<>(true, "Payment status updated successfully", response));
    }
}