package com.bhagavan.payment_gateway_service.repository;

import com.bhagavan.payment_gateway_service.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByPaymentId(Long paymentId);
}