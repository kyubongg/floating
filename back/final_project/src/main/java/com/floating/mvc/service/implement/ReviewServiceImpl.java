package com.floating.mvc.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.floating.mvc.dao.ReviewDao;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.review.GetReviewResponseDto;
import com.floating.mvc.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewDao reviewDao;
	
	@Override
	public ResponseEntity<ResponseDto> insertReview(int planPk) {
		
		try {
			
			int result = reviewDao.insertReview(planPk);
			if(result == 0) return ResponseDto.databaseError();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<? super GetReviewResponseDto> getReview(int planPk) {
		
		GetReviewResponseDto dto = null;
		try {
			
			dto = reviewDao.getReview(planPk);
			if(dto == null) return ResponseDto.noExistReview();
			
		}catch(Exception e) {
			return ResponseDto.databaseError();
		}
		
		return GetReviewResponseDto.success(dto);
	}

	@Override
	public ResponseEntity<ResponseDto> updateReview() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
