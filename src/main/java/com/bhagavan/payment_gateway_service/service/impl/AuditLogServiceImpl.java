package com.bhagavan.payment_gateway_service.service.impl;

import com.bhagavan.payment_gateway_service.dto.response.AuditLogResponse;
import com.bhagavan.payment_gateway_service.entity.AuditLog;
import com.bhagavan.payment_gateway_service.repository.AuditLogRepository;
import com.bhagavan.payment_gateway_service.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void logAction(String entityType, Long entityId, String action, String details) {
        AuditLog log = new AuditLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setDetails(details);
        log.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLogResponse> getLogs(String entityType, Long entityId) {
        List<AuditLog> logs = auditLogRepository
                .findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId);

        List<AuditLogResponse> responses = new ArrayList<>();

        for (AuditLog log : logs) {
            AuditLogResponse response = new AuditLogResponse();
            response.setId(log.getId());
            response.setEntityType(log.getEntityType());
            response.setEntityId(log.getEntityId());
            response.setAction(log.getAction());
            response.setDetails(log.getDetails());
            response.setCreatedAt(log.getCreatedAt());
            responses.add(response);
        }

        return responses;
    }
}