package com.bhagavan.payment_gateway_service.service;

import com.bhagavan.payment_gateway_service.dto.response.AuditLogResponse;

import java.util.List;

public interface AuditLogService {
    void logAction(String entityType, Long entityId, String action, String details);
    List<AuditLogResponse> getLogs(String entityType, Long entityId);
}