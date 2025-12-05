package com.floating.mvc.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.floating.mvc.dao.ReviewDao;
import com.floating.mvc.dto.request.review.PutReviewRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.review.GetReviewResponseDto;
import com.floating.mvc.dto.response.review.ImageInfoDto;
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
	@Transactional
	public ResponseEntity<ResponseDto> updateReview(PutReviewRequestDto dto) {
		
		try {
			
			int reviewUpdateResult = reviewDao.updateReview(dto);
			if(reviewUpdateResult == 0) return ResponseDto.noExistReview();
			
			
			// DB에 저장된 기존 이미지 PK 목록 조회
			List<Integer> existImgPks = reviewDao.selectImgPksByReviewPk(reviewUpdateResult);
			
			// 요청 DTO에 포함된 이미지 PK 목록 추출
			List<Integer> putImgPks = new ArrayList<>();
			for(ImageInfoDto img : dto.getImagePaths()) {
				// pk가 0이 아닌 것만 유지할 목록에 추가
				if(img.getImgPk() != 0) {
					putImgPks.add(img.getImgPk());
				}
			}
			
			// 삭제할 이미지 식별
			List<Integer> deletePks = new ArrayList<>();
			for(Integer existImgPk : existImgPks) {
				if(!putImgPks.contains(existImgPk)) deletePks.add(existImgPk);
			}
			
			if(!deletePks.isEmpty()) {
				reviewDao.deleteImgsByPks(deletePks);
			}
			
			// 새 이미지 추가
			List<ImageInfoDto> newImages = new ArrayList<>();
			for(ImageInfoDto img : dto.getImagePaths()) {
				if(img.getImgPk() == 0) {
					newImages.add(img);
				}
			}
			
			if(!newImages.isEmpty()) {
				reviewDao.insertNewImages(dto.getReviewPk(), newImages);
			}
				
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

	

}
