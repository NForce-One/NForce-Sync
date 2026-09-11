-- Widen eod_entry_day_type_check to accept the new WEEKEND day type.
--
-- EodEntry.DayType has grown WEEKEND (optional overtime logging on a non-working Saturday/Sunday).
-- The constraint installed by V84 only allows
-- ('WORKING_DAY', 'FIRST_HALF_LEAVE', 'SECOND_HALF_LEAVE', 'LEAVE', 'HOLIDAY'), so any submission
-- using WEEKEND would be rejected at the DB layer with a DataIntegrityViolationException before
-- application code ever gets a chance to validate it — same class of failure V84 itself fixed.
ALTER TABLE eod_entry DROP CONSTRAINT IF EXISTS eod_entry_day_type_check;

ALTER TABLE eod_entry ADD CONSTRAINT eod_entry_day_type_check
    CHECK (day_type IN ('WORKING_DAY', 'FIRST_HALF_LEAVE', 'SECOND_HALF_LEAVE', 'LEAVE', 'HOLIDAY', 'WEEKEND'));
