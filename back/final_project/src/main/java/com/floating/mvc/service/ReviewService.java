package com.floating.mvc.service;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.review.PutReviewRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.review.GetReviewResponseDto;

public interface ReviewService {
	
	// 리뷰 삽입
	ResponseEntity<ResponseDto> insertReview(int planPk);
	// 리뷰 조회
	ResponseEntity<? super GetReviewResponseDto> getReviews(String userId);
	// 리뷰 수정
	ResponseEntity<ResponseDto> updateReview(PutReviewRequestDto dto);
}
