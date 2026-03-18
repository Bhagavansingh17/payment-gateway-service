package com.bhagavan.payment_gateway_service.repository;

import com.bhagavan.payment_gateway_service.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByMerchantCode(String merchantCode);
    boolean existsByEmail(String email);
}