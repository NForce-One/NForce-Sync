-- Adds the ADMIN role: user administration (create/edit/activate/deactivate/reset-password/
-- role-assignment) is split off from Super Admin, which keeps system-wide operational
-- oversight only. Existing Super Admin accounts are left untouched (see UserService —
-- SUPERADMIN is no longer a creatable/assignable role going forward, same treatment as the
-- other legacy roles).

ALTER TABLE app_user DROP CONSTRAINT app_user_role_check;

ALTER TABLE app_user
    ADD CONSTRAINT app_user_role_check
    CHECK (role IN ('EMPLOYEE','MANAGER','HR','SUPERADMIN','PM','DM','FINANCE','LEADERSHIP','ADMIN'));

-- Seed a test Admin user so the new role is immediately usable (mirrors V13's pattern).
-- Password hash: BCryptPasswordEncoder(10) of "ChangeMe123!" — same hash reused by the other
-- seeded test users. Idempotent — only inserts if this account doesn't already exist.
INSERT INTO app_user (full_name, email, password_hash, role, status, created_at, manager_id, employee_code)
SELECT 'User Admin', 'useradmin@nforceone.com',
       '$2a$10$eLFmSIWqtvZ05vyxM5UZauimr5UdTqFevbYIgH.KKyjNrRAlIeRp6',
       'ADMIN', 'ACTIVE', NOW(),
       (SELECT id FROM app_user WHERE role = 'SUPERADMIN' AND status = 'ACTIVE' ORDER BY id LIMIT 1),
       'NF-00099001'
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE email = 'useradmin@nforceone.com');
