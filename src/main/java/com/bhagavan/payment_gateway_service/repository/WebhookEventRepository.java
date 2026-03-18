package com.bhagavan.payment_gateway_service.repository;

import com.bhagavan.payment_gateway_service.entity.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, Long> {
    List<WebhookEvent> findByPaymentId(Long paymentId);
}