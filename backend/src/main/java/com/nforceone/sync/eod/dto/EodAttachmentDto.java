package com.nforceone.sync.eod.dto;

import com.nforceone.sync.eod.EodAttachment;
import com.nforceone.sync.eod.EodAttachmentRow;

import java.time.OffsetDateTime;

/** Metadata only — never carries the file bytes. Those are fetched on demand via the download
 *  endpoint, same pattern as BlockerAttachmentDto. */
public record EodAttachmentDto(
        Long           id,
        String         fileName,
        String         contentType,
        Long           fileSize,
        /** Null for an EOD-level attachment. */
        Long           taskId,
        String         uploadedByName,
        OffsetDateTime createdAt
) {
    public static EodAttachmentDto from(EodAttachment a) {
        return new EodAttachmentDto(
                a.getId(),
                a.getOriginalFileName(),
                a.getContentType(),
                a.getFileSize(),
                a.getTask() != null ? a.getTask().getId() : null,
                a.getUploadedBy() != null ? a.getUploadedBy().getFullName() : null,
                a.getCreatedAt()
        );
    }

    /** From the metadata-only projection row — see EodAttachmentRepository.findByEodEntryIdIn. */
    public static EodAttachmentDto from(EodAttachmentRow r) {
        return new EodAttachmentDto(
                r.id(), r.fileName(), r.contentType(), r.fileSize(), r.taskId(), r.uploadedByName(), r.createdAt());
    }
}
