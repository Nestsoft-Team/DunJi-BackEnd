package com.dungzi.backend.domain.review.dao;

import com.dungzi.backend.domain.review.domain.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewReportDao extends JpaRepository<ReviewReport, UUID> {
}
