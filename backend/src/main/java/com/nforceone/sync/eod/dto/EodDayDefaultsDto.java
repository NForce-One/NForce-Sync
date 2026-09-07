package com.nforceone.sync.eod.dto;

/**
 * Day Type / Work Location defaults for a given entry date, so the Submit EOD form can
 * auto-populate them instead of the employee re-entering what the system already knows.
 *
 * Only ever consulted when the form has no existing saved entry for the date — a saved entry's
 * own values always win. Holiday is the only source of truth currently available to override the
 * plain "working day in the office" default: there is no Leave-records table (leave is a
 * self-reported day type, not a pre-existing record) and no per-date/location shift assignment.
 */
public record EodDayDefaultsDto(
        String dayType,
        String workLocation
) {
}
