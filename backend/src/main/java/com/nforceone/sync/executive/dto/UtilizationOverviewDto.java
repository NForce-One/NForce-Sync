package com.nforceone.sync.executive.dto;

import java.math.BigDecimal;
import java.util.List;

// Thresholds are BusinessRuleConfig.underutilizedThresholdPct/overloadedThresholdPct — the
// same configurable business rule the Team Lead dashboard already uses (TeamLeadService) —
// reused here rather than inventing new dashboard-only thresholds.
public record UtilizationOverviewDto(
        BigDecimal overallUtilizationPct,
        BigDecimal totalProductiveHours,
        BigDecimal totalAvailableHours,
        int underutilizedCount,
        int overloadedCount,
        BigDecimal underutilizedThresholdPct,
        BigDecimal overloadedThresholdPct,
        List<EmployeeUtilizationDto> topUtilized,
        List<EmployeeUtilizationDto> bottomUtilized,
        List<UtilizationTrendPointDto> trend
) {}
