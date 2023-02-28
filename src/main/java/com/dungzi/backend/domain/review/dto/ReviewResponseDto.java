package com.dungzi.backend.domain.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDto {
    private String address;

    private int totalRate;

    private String image;

    private long count;
}