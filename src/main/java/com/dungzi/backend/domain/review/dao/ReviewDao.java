package com.dungzi.backend.domain.review.dao;

import com.dungzi.backend.domain.review.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewDao extends JpaRepository<Review, UUID> {
    Optional<Review> findByAddress(String address);
    List<Review> findByAddress(String address, Pageable pageable);
}
