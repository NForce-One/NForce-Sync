package com.nforceone.sync.executive.dto;

import java.util.Map;

// Statuses are Project.Status's actual values (ACTIVE, INACTIVE, COMPLETED, ON_HOLD) — no
// "Upcoming" status exists anywhere in the application, so none is fabricated here.
public record ProjectPortfolioDto(
        long totalProjects,
        long activeProjects,
        long onHoldProjects,
        long completedProjects,
        long inactiveProjects,
        Map<String, Long> byStatus
) {}
