package com.nforceone.sync.executive.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UtilizationTrendPointDto(LocalDate date, BigDecimal utilizationPct) {}
