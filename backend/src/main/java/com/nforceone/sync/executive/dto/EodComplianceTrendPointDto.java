package com.nforceone.sync.executive.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EodComplianceTrendPointDto(
        LocalDate date,
        long expected,
        long submitted,
        long missing,
        BigDecimal compliancePct
) {}
