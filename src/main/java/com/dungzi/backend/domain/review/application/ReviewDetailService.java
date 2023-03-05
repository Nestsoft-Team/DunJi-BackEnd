package com.dungzi.backend.domain.review.application;

import com.dungzi.backend.domain.review.dao.ReviewDao;
import com.dungzi.backend.domain.review.dao.ReviewDetailDao;
import com.dungzi.backend.domain.review.dao.ReviewReportDao;
import com.dungzi.backend.domain.review.domain.Review;
import com.dungzi.backend.domain.review.domain.ReviewDetail;
import com.dungzi.backend.domain.review.dto.ReviewDetailResponseDto;
import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
import com.dungzi.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewDetailService {
    private final ReviewDetailDao reviewDetailDao;

    private final ReviewDao reviewDao;

    private final ReviewReportDao reviewReportDao;

    public ReviewDetail saveReviewDetail(ReviewDetail reviewDetail){
        return reviewDetailDao.save(reviewDetail);
    }

    public void deleteReviewDetail(String reviewDetailId){
        ReviewDetail reviewDetail = reviewDetailDao.findById(UUID.fromString(reviewDetailId)).get();
        Review review = reviewDao.findById(reviewDetail.getBuildingId()).get();
        int curCount = reviewDetailDao.countByBuildingId(reviewDetail.getBuildingId());

        review.deleteReview(reviewDetail,curCount);
        reviewDetailDao.deleteById(UUID.fromString(reviewDetailId));
    }

    public List<ReviewDetailResponseDto> getReviewDetailList(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("regDate").descending());
        return ReviewDetailResponseDto.changeToReviewDetailResponseDto(reviewDetailDao.findAll(pageRequest).toList());
    }

    public List<ReviewDetailResponseDto> getReviewDetail(String buildingId,Pageable pageable){
        return ReviewDetailResponseDto.changeToReviewDetailResponseDto(reviewDetailDao.findByBuildingId(UUID.fromString(buildingId),pageable));
    }

    public void reportReviewDetail(ReviewRequestDto.ReportReview requestDto, User user){
        reviewReportDao.save(requestDto.toReportEntity(requestDto.getReviewId(),requestDto.getReportType(),user.getUserId()));
    }
}
