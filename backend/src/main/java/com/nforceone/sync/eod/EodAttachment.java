package com.nforceone.sync.eod;

import com.nforceone.sync.auth.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

/**
 * A supporting file attached either to the overall EOD entry ({@code task} null) or to one of
 * its task rows ({@code task} set). See V74 migration for why {@code task}'s FK is
 * ON DELETE SET NULL rather than CASCADE.
 */
@Entity
@Table(name = "eod_attachment")
@Getter
@Setter
public class EodAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eod_entry_id", nullable = false)
    private EodEntry eodEntry;

    /** Null = EOD-level attachment. Set = belongs to this specific task row. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eod_task_id")
    private EodTask task;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    /** Server-generated (UUID-based) — never derived from client input. Metadata only; actual
     *  retrieval is always by this row's numeric {@link #id}, never by filename. */
    @Column(name = "stored_file_name", nullable = false)
    private String storedFileName;

    @Column(name = "content_type", nullable = false, length = 150)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    // @Lob (deliberately not used) maps byte[] to Types.BLOB, which Hibernate/the PG JDBC driver
    // bind as a Large Object OID (bigint) rather than this column's actual `bytea` type — see
    // the identical bug fixed in BlockerReplyAttachment.data. VARBINARY is the correct mapping.
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(nullable = false)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    private AppUser uploadedBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
}
