-- Widen eod_entry_day_type_check to accept the two half-day leave types.
--
-- The live Neon DB's constraint (installed as V83, applied directly and never committed —
-- see backend/CLAUDE.md's "Flyway: the DB is AHEAD of this repo") only allows
-- ('WORKING_DAY', 'LEAVE', 'HOLIDAY'). EodEntry.DayType has since grown FIRST_HALF_LEAVE and
-- SECOND_HALF_LEAVE, so any submission using either value is rejected at the DB layer with a
-- DataIntegrityViolationException (surfaces to the client as a 500) before application code
-- ever gets a chance to validate it.
--
-- Numbered V84, ABOVE the live schema's recorded version 83 — check
-- flyway_schema_history before adding the next migration after this one.
ALTER TABLE eod_entry DROP CONSTRAINT IF EXISTS eod_entry_day_type_check;

ALTER TABLE eod_entry ADD CONSTRAINT eod_entry_day_type_check
    CHECK (day_type IN ('WORKING_DAY', 'FIRST_HALF_LEAVE', 'SECOND_HALF_LEAVE', 'LEAVE', 'HOLIDAY'));
