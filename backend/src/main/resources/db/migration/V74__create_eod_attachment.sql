-- EOD Supporting Attachments: optional files attached either to the overall EOD entry
-- (eod_task_id NULL) or to an individual task row (eod_task_id set).
--
-- eod_task_id is ON DELETE SET NULL, not CASCADE: EodService.saveDraft destroys and recreates
-- every eod_task row on every draft save (orphanRemoval), so a CASCADE here would silently
-- delete a task-level attachment the next time the employee edits an unrelated field. SET NULL
-- lets the attachment survive as an EOD-level row for the instant before the same transaction
-- re-points it to the freshly-created task (see EodAttachmentService.reassignForSave).
CREATE TABLE eod_attachment (
    id                  BIGSERIAL PRIMARY KEY,
    eod_entry_id        BIGINT NOT NULL REFERENCES eod_entry(id) ON DELETE CASCADE,
    eod_task_id         BIGINT REFERENCES eod_task(id) ON DELETE SET NULL,
    original_file_name  VARCHAR(255) NOT NULL,
    stored_file_name    VARCHAR(255) NOT NULL,
    content_type        VARCHAR(150) NOT NULL,
    file_size           BIGINT NOT NULL,
    data                BYTEA NOT NULL,
    uploaded_by_id       BIGINT NOT NULL REFERENCES app_user(id),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_eod_attachment_entry ON eod_attachment(eod_entry_id);
CREATE INDEX idx_eod_attachment_task  ON eod_attachment(eod_task_id);
