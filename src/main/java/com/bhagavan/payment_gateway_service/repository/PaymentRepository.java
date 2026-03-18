package com.bhagavan.payment_gateway_service.repository;

import com.bhagavan.payment_gateway_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentRef(String paymentRef);
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}