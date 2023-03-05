package com.dungzi.backend.domain.review.dto;

import com.dungzi.backend.domain.review.domain.ReportType;
import com.dungzi.backend.domain.review.domain.Review;
import com.dungzi.backend.domain.review.domain.ReviewDetail;
import com.dungzi.backend.domain.review.domain.ReviewReport;
import com.dungzi.backend.domain.user.domain.User;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class ReviewRequestDto {

    @Data
    public static class CreateReview{

        private String content;

        @NotBlank
        private String address;

        private String addressDetail;

        @NotBlank
        private float totalRate;

        @NotBlank
        private float cleanRate;

        @NotBlank
        private float noiseRate;

        @NotBlank
        private float accessRate;

        @NotBlank
        private float hostRate;

        @NotBlank
        private float facilityRate;

        private String period;

        public ReviewDetail toReviewDetailEntity(UUID buildingId, User user){
            return ReviewDetail.builder()
                    .user(user)
                    .buildingId(buildingId)
                    .content(this.content)
                    .address(this.address)
                    .addressDetail(this.addressDetail)
                    .totalRate(this.totalRate)
                    .cleanRate(this.cleanRate)
                    .noiseRate(this.noiseRate)
                    .accessRate(this.accessRate)
                    .hostRate(this.hostRate)
                    .facilityRate(this.facilityRate)
                    .period(this.period)
                    .build();

        }
        public Review toReviewEntity(){
            return Review.builder()
                    .address(this.address)
                    .totalRate(this.totalRate)
                    .cleanRate(this.cleanRate)
                    .noiseRate(this.noiseRate)
                    .accessRate(this.accessRate)
                    .hostRate(this.hostRate)
                    .facilityRate(this.facilityRate)
                    .build();

        }
    }

    @Data
    public static class ReportReview{
        private UUID reviewId;

        private ReportType reportType;

        public ReviewReport toReportEntity(UUID reviewId, ReportType reportType,UUID userId){
            return ReviewReport.builder()
                    .userId(userId)
                    .reviewId(reviewId)
                    .reportType(reportType)
                    .build();
        }

    }

}
