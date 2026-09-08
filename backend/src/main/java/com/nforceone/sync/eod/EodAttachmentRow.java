package com.nforceone.sync.eod;

import java.time.OffsetDateTime;

/** Metadata-only projection row for {@link EodAttachmentRepository#findByEodEntryIdIn} — never
 *  carries the file bytes. See that method's note for why this projection exists. */
public record EodAttachmentRow(
        Long id, Long eodEntryId, Long taskId, String fileName, String contentType, Long fileSize,
        String uploadedByName, OffsetDateTime createdAt
) {}
