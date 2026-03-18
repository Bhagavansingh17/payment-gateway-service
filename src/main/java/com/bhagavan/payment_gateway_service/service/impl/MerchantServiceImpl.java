package com.bhagavan.payment_gateway_service.service.impl;

import com.bhagavan.payment_gateway_service.dto.request.CreateMerchantRequest;
import com.bhagavan.payment_gateway_service.dto.response.MerchantResponse;
import com.bhagavan.payment_gateway_service.entity.Merchant;
import com.bhagavan.payment_gateway_service.enums.MerchantStatus;
import com.bhagavan.payment_gateway_service.exception.DuplicateResourceException;
import com.bhagavan.payment_gateway_service.exception.ResourceNotFoundException;
import com.bhagavan.payment_gateway_service.repository.MerchantRepository;
import com.bhagavan.payment_gateway_service.service.MerchantService;
import org.springframework.stereotype.Service;
import com.bhagavan.payment_gateway_service.service.AuditLogService;

import java.time.LocalDateTime;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final AuditLogService auditLogService;

    public MerchantServiceImpl(MerchantRepository merchantRepository,
                               AuditLogService auditLogService) {
        this.merchantRepository = merchantRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public MerchantResponse createMerchant(CreateMerchantRequest request) {
        if (merchantRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Merchant with this email already exists");
        }

        Merchant merchant = new Merchant();
        merchant.setMerchantCode(request.getMerchantCode());
        merchant.setName(request.getName());
        merchant.setEmail(request.getEmail());
        merchant.setStatus(MerchantStatus.ACTIVE);
        merchant.setCreatedAt(LocalDateTime.now());
        merchant.setUpdatedAt(LocalDateTime.now());

        Merchant savedMerchant = merchantRepository.save(merchant);

        auditLogService.logAction(
                "MERCHANT",
                savedMerchant.getId(),
                "CREATE",
                "Merchant created with code " + savedMerchant.getMerchantCode()
        );

        return mapToResponse(savedMerchant);
    }

    @Override
    public MerchantResponse getMerchantById(Long id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + id));

        return mapToResponse(merchant);
    }

    private MerchantResponse mapToResponse(Merchant merchant) {
        MerchantResponse response = new MerchantResponse();
        response.setId(merchant.getId());
        response.setMerchantCode(merchant.getMerchantCode());
        response.setName(merchant.getName());
        response.setEmail(merchant.getEmail());
        response.setStatus(merchant.getStatus());
        response.setCreatedAt(merchant.getCreatedAt());
        return response;
    }
}