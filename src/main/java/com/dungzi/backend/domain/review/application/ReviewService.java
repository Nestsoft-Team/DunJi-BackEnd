package com.dungzi.backend.domain.review.application;

import com.dungzi.backend.domain.review.dao.ReviewDao;
import com.dungzi.backend.domain.review.dao.ReviewDetailDao;
import com.dungzi.backend.domain.review.domain.Review;
import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
import com.dungzi.backend.domain.review.dto.ReviewResponseDto;
import com.dungzi.backend.global.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewService {

    private final ReviewDetailDao reviewDetailDao;
    private final ReviewDao reviewDao;


    private final FileUploadService fileUploadService;

    @Transactional
    public UUID saveReview(ReviewRequestDto.CreateReview requestDto, List<MultipartFile> files){
        Optional<Review> findReview = reviewDao.findByAddress(requestDto.getAddress());
        UUID reviewId;
        if (findReview.isEmpty()) { //해당주소의 객체가 없다면
            reviewId = reviewDao.save(requestDto.toReviewEntity()).getReviewId();//새로운 객체 만들기
        }
        else{ //이미 해당주소의 객체가 있다면
            Review review = findReview.get();
            int curCount = reviewDetailDao.countByBuildingId(review.getReviewId());
            review.updateReview(requestDto,curCount);
            reviewId = review.getReviewId();
        }
        fileUploadService.uploadReviewFile(reviewId.toString(), files);
        return reviewId;
    }

    public List<ReviewResponseDto> findReview(String address,Pageable pageable){
        int curCount = reviewDetailDao.countByAddress(address);
        return ReviewResponseDto.changeToReviewResponseDto(reviewDao.findByAddress(address,pageable),curCount);
    }
}
