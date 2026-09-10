-- Fixes the common cause of "Failed to load ... Dashboard" across the Executive, Team Lead and
-- Project Dashboard screens: a single eod_entry row (id 193, employee 56, 2026-09-10) was written
-- with day_type = 'FIRST_HALF_LEAVE', a value with no corresponding Java DayType constant
-- (WORKING_DAY, LEAVE, HOLIDAY — see EodEntry.java). It was introduced out-of-band, directly
-- against this shared database — no migration in this repo's history ever added it (same class of
-- drift as V73's billing_model_id and V78-81's assistant tables).
--
-- Hibernate throws IllegalArgumentException hydrating ANY EodEntry row with an unmapped enum
-- value. Every dashboard that loads full EOD entries for a date range covering "today" —
-- ExecutiveDashboardService (via TeamMissingEodReportService), TeamLeadController's dashboard
-- summary, and ProjectDashboardService — hit this same row and failed the same way, which is why
-- the failure looked application-wide rather than isolated to one screen.
--
-- Remapped to LEAVE, the closest value the application actually supports (the entry is clearly
-- some form of leave, not a normal working day) — no business data is deleted, only corrected to
-- a value the current codebase understands. A CHECK constraint is then added, mirroring the
-- existing eod_entry_status_check pattern on this same table, so any future out-of-band value
-- fails fast at write time instead of silently crashing every downstream dashboard read.
UPDATE eod_entry SET day_type = 'LEAVE' WHERE day_type NOT IN ('WORKING_DAY', 'LEAVE', 'HOLIDAY');

ALTER TABLE eod_entry
    ADD CONSTRAINT eod_entry_day_type_check
    CHECK (day_type IN ('WORKING_DAY', 'LEAVE', 'HOLIDAY'));
