package com.nforceone.sync.eod;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "blocker_reply_attachment")
@Getter
@Setter
public class BlockerReplyAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", nullable = false)
    private BlockerReply reply;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    // @Lob (removed) mapped byte[] to Types.BLOB, which Hibernate/the PG JDBC driver bind as a
    // Large Object OID reference (an int8/bigint handle into pg_largeobject) rather than the raw
    // `bytea` column this migration actually created — every insert failed with "column data is
    // of type bytea but expression is of type bigint". VARBINARY is the correct mapping for a
    // plain bytea column; attachments here are capped at 5MB so no LOB streaming is needed anyway.
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(nullable = false)
    private byte[] data;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
