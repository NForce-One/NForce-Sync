package com.nforceone.sync.eod.dto;

import java.math.BigDecimal;

/**
 * Day Type / Work Location defaults for a given entry date, so the Submit EOD form can
 * auto-populate them instead of the employee re-entering what the system already knows.
 *
 * dayType/workLocation are only ever consulted when the form has no existing saved entry for the
 * date — a saved entry's own values always win. Holiday is the only source of truth currently
 * available to override the plain "working day in the office" default: there is no Leave-records
 * table (leave is a self-reported day type, not a pre-existing record) and no per-date/location
 * shift assignment.
 *
 * workingHoursPerDay is different: it is read and shown on EVERY date, saved entry or not — it is
 * the same Super Admin Business Rules value (business_rule_config.standard_hours_per_day, a
 * single global row, not scoped by role/department/date) that EodService.validateLoggedDay
 * enforces server-side, surfaced here so the "X / Y hrs" readout and the minimum-hours validation
 * on the form can never disagree. Lives on this endpoint rather than a new one so the form does
 * not need a second round trip.
 */
public record EodDayDefaultsDto(
        String dayType,
        String workLocation,
        BigDecimal workingHoursPerDay
) {
}
