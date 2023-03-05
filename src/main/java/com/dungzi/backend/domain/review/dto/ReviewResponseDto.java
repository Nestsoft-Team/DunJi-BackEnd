package com.dungzi.backend.domain.review.dto;

import com.dungzi.backend.domain.review.domain.Review;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ReviewResponseDto {
    private String address;

    private float totalRate;

    private String image;

    private long count;

    static public List<ReviewResponseDto> changeToReviewResponseDto(List<Review> reviewList, int curCount) {
        return reviewList.stream()
                .map(review -> ReviewResponseDto.builder()
                        .address(review.getAddress())
                        .totalRate(review.getTotalRate())
                        .count(curCount)
                        .build()).collect(Collectors.toList());
    }
}
