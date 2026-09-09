package com.nforceone.sync.executive.dto;

import com.nforceone.sync.admin.dto.AuditLogDto;

import java.time.LocalDate;
import java.util.List;

public record ExecutiveDashboardDto(
        LocalDate from,
        LocalDate to,
        WorkforceOverviewDto workforce,
        ProjectPortfolioDto projects,
        EodComplianceDto eodCompliance,
        UtilizationOverviewDto utilization,
        AllocationOverviewDto allocation,
        List<ProjectAttentionDto> projectsRequiringAttention,
        List<AuditLogDto> recentActivity
) {}
