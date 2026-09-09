package com.nforceone.sync.executive.dto;

import java.util.Map;

public record WorkforceOverviewDto(
        long totalUsers,
        long activeUsers,
        long inactiveUsers,
        Map<String, Long> usersByRole
) {}
