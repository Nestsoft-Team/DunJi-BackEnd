package com.dungzi.backend.domain.review.dao;

import com.dungzi.backend.domain.review.domain.ReviewDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewDetailDao extends JpaRepository<ReviewDetail, UUID> {
    int countByBuildingId(UUID buildingId);

    int countByAddress(String address);

    Page<ReviewDetail> findAll(Pageable pageable);

    List<ReviewDetail> findByBuildingId(UUID buildingId,Pageable pageable);

}
