package com.floating.mvc.dao;

import com.floating.mvc.dto.response.review.GetReviewResponseDto;

public interface ReviewDao {
	// 리뷰 row 생성
	int insertReview(int planPk);
	// 리뷰 조회
	GetReviewResponseDto getReview(int reviewPk);
}
