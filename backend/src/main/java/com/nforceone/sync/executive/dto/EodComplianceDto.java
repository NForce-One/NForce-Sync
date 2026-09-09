package com.nforceone.sync.executive.dto;

import java.math.BigDecimal;
import java.util.List;

public record EodComplianceDto(
        long expected,
        long submitted,
        long missing,
        BigDecimal compliancePct,
        List<EodComplianceTrendPointDto> trend
) {}
