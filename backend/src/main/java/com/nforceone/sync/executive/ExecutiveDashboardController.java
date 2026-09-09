package com.nforceone.sync.executive;

import com.nforceone.sync.executive.dto.ExecutiveDashboardDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

// Organization-wide executive oversight — Super Admin only. Distinct from the Admin Dashboard
// (/api/admin/stats, ADMIN-only): this is read-only visibility, no user-administration actions.
@RestController
@RequestMapping("/api/executive")
@PreAuthorize("hasRole('SUPERADMIN')")
public class ExecutiveDashboardController {

    private final ExecutiveDashboardService service;

    public ExecutiveDashboardController(ExecutiveDashboardService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public ExecutiveDashboardDto getDashboard(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getDashboard(actingEmail(), from, to);
    }

    private String actingEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
