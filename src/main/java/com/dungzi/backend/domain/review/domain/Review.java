package com.dungzi.backend.domain.review.domain;

import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
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
public class Review {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID reviewId;

    private String address;

    private float totalRate;

    private float cleanRate;

    private float noiseRate;

    private float accessRate;

    private float hostRate;

    private  float facilityRate;



    public void updateReview(ReviewRequestDto.CreateReview requestDto,int curCount) {
        this.totalRate = calculateUpdateRate(this.totalRate, requestDto.getTotalRate(),curCount);
        this.cleanRate = calculateUpdateRate(this.cleanRate, requestDto.getCleanRate(),curCount);
        this.noiseRate = calculateUpdateRate(this.noiseRate, requestDto.getNoiseRate(),curCount);
        this.accessRate = calculateUpdateRate(this.accessRate, requestDto.getAccessRate(),curCount);
        this.hostRate = calculateUpdateRate(this.hostRate, requestDto.getHostRate(),curCount);
        this.facilityRate = calculateUpdateRate(this.facilityRate, requestDto.getFacilityRate(),curCount);
    }

    public void deleteReview(ReviewDetail review,int curCount) {
        this.totalRate = calculateDeleteRate(this.totalRate, review.getTotalRate(),curCount);
        this.cleanRate = calculateDeleteRate(this.cleanRate, review.getCleanRate(),curCount);
        this.noiseRate = calculateDeleteRate(this.noiseRate, review.getNoiseRate(),curCount);
        this.accessRate = calculateDeleteRate(this.accessRate, review.getAccessRate(),curCount);
        this.hostRate = calculateDeleteRate(this.hostRate, review.getHostRate(),curCount);
        this.facilityRate = calculateDeleteRate(this.facilityRate, review.getFacilityRate(),curCount);
    }

    private float calculateUpdateRate(float total, float data, int curCount) {
        return (total * curCount + data) / (curCount + 1);
    }

    private float calculateDeleteRate(float total, float data, int curCount) {
        return (total * curCount - data) / (curCount - 1);
    }
}
