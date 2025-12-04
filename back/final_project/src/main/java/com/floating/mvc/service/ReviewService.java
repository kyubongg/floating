package com.floating.mvc.service;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.review.GetReviewResponseDto;

public interface ReviewService {
	
	// 리뷰 삽입
	ResponseEntity<ResponseDto> insertReview(int planPk);
	// 리뷰 조회
	ResponseEntity<? super GetReviewResponseDto> getReview(int planPk);
	// 리뷰 수정
	ResponseEntity<ResponseDto> updateReview();
}
