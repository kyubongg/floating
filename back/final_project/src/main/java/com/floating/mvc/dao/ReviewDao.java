package com.floating.mvc.dao;

import java.util.List;

import com.floating.mvc.dto.request.review.PutReviewRequestDto;
import com.floating.mvc.dto.response.review.GetReviewResponseDto;
import com.floating.mvc.dto.response.review.ImageInfoDto;

public interface ReviewDao {
	// 리뷰 row 생성
	int insertReview(int planPk);
	// 리뷰 조회
	GetReviewResponseDto getReview(int reviewPk);
	// 리뷰 수정
	int updateReview(PutReviewRequestDto dto);
	// 이미지 조회
	List<Integer> selectImgPksByReviewPk(int reviewPk);
	// 이미지 삭제
	void deleteImgsByPks(List<Integer> deletePks);
	// 이미지 추가
	void insertNewImages(int reviewPk, List<ImageInfoDto> newImgs);
}
