package com.bhagavan.payment_gateway_service.controller;

import com.bhagavan.payment_gateway_service.dto.response.ApiResponse;
import com.bhagavan.payment_gateway_service.dto.response.AuditLogResponse;
import com.bhagavan.payment_gateway_service.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/{entityType}/{entityId}")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getLogs(
            @PathVariable String entityType,
            @PathVariable Long entityId) {

        List<AuditLogResponse> response = auditLogService.getLogs(entityType.toUpperCase(), entityId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Audit logs fetched successfully", response)
        );
    }
}