package com.nforceone.sync.eod.dto;

import com.nforceone.sync.eod.EodEntry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record SaveEodRequest(
        @NotNull(message = "Entry date is required")
        LocalDate entryDate,

        /** Null is tolerated and treated as WORKING_DAY, so older clients keep working. */
        EodEntry.DayType dayType,

        /** Time adjustment, WORKING_DAY only. Both null when none is requested. */
        EodEntry.TimeAdjustmentType timeAdjustmentType,
        Integer timeAdjustmentMinutes,

        String workLocation,

        /** Capped at 300 characters, matching MAX_TEXT_LEN on the Submit EOD form. */
        @Size(max = 300) String nextDayPlan,
        @Size(max = 300) String remarks,

        /** @Valid is required for the per-task constraints (description/blockerReason length) to
         *  be checked at all — without it, Bean Validation does not descend into the list. */
        @Valid List<SaveEodTaskRequest> tasks,

        /** IDs of attachments (already uploaded via the separate upload endpoint) that should be
         *  associated with the overall EOD entry — null/empty is fine. See
         *  EodAttachmentService.reassignForSave for how these get (re-)pointed on every save. */
        List<Long> attachmentIds
) {}
