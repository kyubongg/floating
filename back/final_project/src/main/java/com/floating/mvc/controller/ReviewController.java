package com.floating.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floating.mvc.dto.request.review.PutReviewRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.review.GetReviewResponseDto;
import com.floating.mvc.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

	private final ReviewService reviewService;
	
	@GetMapping("/{planPk}")
	public ResponseEntity<? super GetReviewResponseDto> getReview(
			@PathVariable int planPk
	){
		ResponseEntity<? super GetReviewResponseDto> response = reviewService.getReview(planPk);
		return response;
	}
	
	@PutMapping("/{planPk}")
	public ResponseEntity<ResponseDto> putReview(
			@RequestBody PutReviewRequestDto dto
	){
		ResponseEntity<ResponseDto> response = reviewService.updateReview(dto);
		return response;
	}
	
	
}
