package com.floating.mvc.service.implement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.floating.mvc.dao.ReviewDao;
import com.floating.mvc.dto.request.review.PutReviewRequestDto;
import com.floating.mvc.dto.request.review.ReviewRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.review.GetReviewResponseDto;
import com.floating.mvc.dto.response.review.ImageInfoDto;
import com.floating.mvc.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	@Value("${file.upload-dir}")
	private String uploadDir;
	
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
	public ResponseEntity<? super GetReviewResponseDto> getReviews(String userId) {
		
		List<ReviewRequestDto> reviewList = null;
		try {
			
			reviewList = reviewDao.getReviews(userId);
			if(reviewList == null) return ResponseDto.noExistReview();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return GetReviewResponseDto.success(reviewList);
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseDto> updateReview(PutReviewRequestDto dto) {
		
		try {
			
			int reviewUpdateResult = reviewDao.updateReview(dto);
			if(reviewUpdateResult == 0) return ResponseDto.noExistReview();
			
			List<ImageInfoDto> requestImagePaths = (dto.getImgPaths() == null)
													? new ArrayList<>()
													: dto.getImgPaths();
			
			// DB에 저장된 기존 이미지 PK 목록 조회
			List<Integer> existImgPks = reviewDao.selectImgPksByReviewPk(dto.getReviewPk());
			
			// 요청 DTO에 포함된 이미지 PK 목록 추출
			List<Integer> putImgPks = requestImagePaths.stream()
			        .map(ImageInfoDto::getImgPk)
			        .filter(pk -> pk != null && pk != 0)
			        .collect(Collectors.toList());
			
			// 삭제할 이미지 식별
			List<Integer> deletePks = new ArrayList<>();
			for(Integer existImgPk : existImgPks) {
				if(!putImgPks.contains(existImgPk)) deletePks.add(existImgPk);
			}
			
			if(!deletePks.isEmpty()) {
				reviewDao.deleteImgsByPks(deletePks);
			}
			

			// 새 이미지 물리 파일 저장 및 DB 등
			if(dto.getImages() != null && !dto.getImages().isEmpty()) {
				
				List<String> savedPaths = new ArrayList<>();
				
				File directory = new File(uploadDir);

			    if (!directory.exists()) {
			        boolean isCreated = directory.mkdirs(); // mkdir()이 아니라 mkdirs()를 써야 하위까지 다 만듭니다.
			        if (isCreated) {
			            System.out.println("폴더가 생성되었습니다: " + uploadDir);
			        }
			    }
				
				for(MultipartFile file: dto.getImages()) {
					
					if (!file.isEmpty()) {
						String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			            File saveFile = new File(uploadDir, fileName);
			            file.transferTo(saveFile);
			            
			            savedPaths.add("/images/reviews/" + fileName);
					}
				}
				
				if(!savedPaths.isEmpty()) {
					reviewDao.insertNewImages(dto.getReviewPk(), savedPaths);
				}
			}
				
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

	

}
