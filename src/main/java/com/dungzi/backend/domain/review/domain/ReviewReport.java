package com.dungzi.backend.domain.review.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReport {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID reportId;

    @Column(nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID reviewId;

    @Column(length = 36)
    @Type(type = "uuid-char")
    private UUID userId;

    @Enumerated(value = EnumType.STRING)
    private ReportType reportType;

    @Column(columnDefinition = "boolean default 0")
    private boolean isDeleted;

}
