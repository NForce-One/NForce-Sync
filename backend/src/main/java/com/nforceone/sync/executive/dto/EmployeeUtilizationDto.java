package com.nforceone.sync.executive.dto;

import java.math.BigDecimal;

public record EmployeeUtilizationDto(
        Long employeeId,
        String fullName,
        String employeeCode,
        BigDecimal utilizationPct,
        String primaryProject
) {}
