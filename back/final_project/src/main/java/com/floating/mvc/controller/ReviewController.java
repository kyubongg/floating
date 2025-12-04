package com.floating.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
}
