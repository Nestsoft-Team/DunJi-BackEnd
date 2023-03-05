package com.dungzi.backend.domain.review.dto;

import com.dungzi.backend.domain.review.domain.ReviewDetail;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class ReviewDetailResponseDto {
    private UUID reviewDetailId;

    private String userNickname;

    private String content;

    private String address;

    private String addressDetail;

    private String period;

    private String regDate;

    private List<String> image;

    private float totalRate;

    static public String zonedDateTimeToDateTime(final ZonedDateTime zdt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.M.dd. a hh:mm");
        return zdt.format(formatter);
    }

    static public List<ReviewDetailResponseDto> changeToReviewDetailResponseDto(List<ReviewDetail> reviewList) {
        return reviewList.stream()
                .map(reviewDetail -> ReviewDetailResponseDto.builder()
                        .userNickname(reviewDetail.getUser().getNickname())
                        .content(reviewDetail.getContent())
                        .address(reviewDetail.getAddress())
                        .addressDetail(reviewDetail.getAddressDetail())
                        .period(reviewDetail.getPeriod())
                        .totalRate(reviewDetail.getTotalRate())
                        .regDate(ReviewDetailResponseDto.zonedDateTimeToDateTime(reviewDetail.getRegDate()))
                        .build()).collect(Collectors.toList());
    }

}
