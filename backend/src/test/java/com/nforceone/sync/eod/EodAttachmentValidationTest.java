package com.nforceone.sync.eod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EodAttachmentValidationTest {

    private static final long MAX_BYTES = 10L * 1024 * 1024;

    @Test
    void png_within_size_limit_passes() {
        assertNull(EodAttachmentValidation.validate("evidence.png", "image/png", 1024, MAX_BYTES));
    }

    @Test
    void pdf_within_size_limit_passes() {
        assertNull(EodAttachmentValidation.validate("report.pdf", "application/pdf", MAX_BYTES, MAX_BYTES));
    }

    @Test
    void docx_passes() {
        assertNull(EodAttachmentValidation.validate("notes.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 2048, MAX_BYTES));
    }

    @Test
    void unsupported_type_is_rejected_with_named_types_message() {
        String err = EodAttachmentValidation.validate("malware.exe", "application/x-msdownload", 100, MAX_BYTES);
        assertNotNull(err);
        assertTrue(err.contains("not a supported file type"));
        assertTrue(err.contains("PNG"));
        assertTrue(err.contains("XLS"));
    }

    @Test
    void null_content_type_is_rejected_not_trusted_as_fine() {
        assertNotNull(EodAttachmentValidation.validate("mystery", null, 100, MAX_BYTES));
    }

    @Test
    void oversized_file_is_rejected_even_with_allowed_type() {
        String err = EodAttachmentValidation.validate("huge.png", "image/png", MAX_BYTES + 1, MAX_BYTES);
        assertNotNull(err);
        assertTrue(err.contains("exceeds"));
    }

    @Test
    void file_exactly_at_the_size_limit_passes() {
        assertNull(EodAttachmentValidation.validate("edge.pdf", "application/pdf", MAX_BYTES, MAX_BYTES));
    }

    @Test
    void content_type_matching_is_case_insensitive() {
        assertNull(EodAttachmentValidation.validate("shout.PNG", "IMAGE/PNG", 100, MAX_BYTES));
    }

    // ── exceedsStorageCap ──────────────────────────────────────────────

    private static final long MAX_TOTAL = 2L * 1024 * 1024 * 1024; // 2 GB, matches the app default

    @Test
    void upload_within_remaining_capacity_is_allowed() {
        assertFalse(EodAttachmentValidation.exceedsStorageCap(MAX_TOTAL - 100, 50, MAX_TOTAL));
    }

    @Test
    void upload_that_would_exactly_fill_capacity_is_allowed() {
        assertFalse(EodAttachmentValidation.exceedsStorageCap(MAX_TOTAL - 100, 100, MAX_TOTAL));
    }

    @Test
    void upload_that_would_exceed_capacity_is_rejected() {
        assertTrue(EodAttachmentValidation.exceedsStorageCap(MAX_TOTAL - 100, 101, MAX_TOTAL));
    }

    @Test
    void already_over_capacity_rejects_any_further_upload() {
        assertTrue(EodAttachmentValidation.exceedsStorageCap(MAX_TOTAL + 1, 1, MAX_TOTAL));
    }

    @Test
    void empty_store_allows_an_upload_within_the_cap() {
        assertFalse(EodAttachmentValidation.exceedsStorageCap(0, MAX_BYTES, MAX_TOTAL));
    }
}
