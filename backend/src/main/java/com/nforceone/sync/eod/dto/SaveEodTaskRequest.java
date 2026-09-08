package com.nforceone.sync.eod.dto;

import com.nforceone.sync.eod.EodTask;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

/** Free-text fields are capped at 300 characters, matching MAX_TEXT_LEN on the Submit EOD form. */
public record SaveEodTaskRequest(
        Long              projectId,
        Long              taskCategoryId,
        @Size(max = 300)  String description,
        BigDecimal        hours,
        EodTask.TaskStatus taskStatus,
        @Size(max = 300)  String blockerReason,
        String            supportNeeded,

        /** IDs of attachments that should be associated with THIS task row — null/empty is fine.
         *  Since task rows are destroyed and recreated on every save (see EodService.saveDraft),
         *  this list is how a task-level attachment survives across saves: EodAttachmentService
         *  re-points these IDs to the freshly-created task row right after it's persisted. */
        List<Long>        attachmentIds
) {}
