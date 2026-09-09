package com.nforceone.sync.executive.dto;

public record ProjectAttentionDto(
        Long projectId,
        String projectName,
        String projectManagerName,
        String status,
        String metric,
        String reason
) {}
