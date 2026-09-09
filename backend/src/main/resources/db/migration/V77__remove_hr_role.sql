-- Removes the HR role entirely from the application. The 2 existing HR-role users
-- (already soft-deleted — status=INACTIVE, deleted_at set) are reassigned to EMPLOYEE so no row
-- references 'HR' before the CHECK constraint is narrowed. manager_id is cleared since their old
-- manager (a Super Admin, per HR's former hierarchy) is not a valid Employee-role manager — same
-- "safe null-out" approach V65 used for a similar historical inconsistency; these accounts are
-- deactivated, so this is a data-hygiene cleanup, not a live reporting-relationship change.
UPDATE app_user
SET role = 'EMPLOYEE', manager_id = NULL
WHERE role = 'HR';

ALTER TABLE app_user DROP CONSTRAINT app_user_role_check;

ALTER TABLE app_user
    ADD CONSTRAINT app_user_role_check
    CHECK (role IN ('EMPLOYEE','MANAGER','SUPERADMIN','PM','DM','FINANCE','LEADERSHIP','ADMIN'));
