package com.floating.mvc.dto.response.review;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.review.ImageInfoRequestDto;
import com.floating.mvc.dto.request.review.ReviewRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetReviewResponseDto extends ResponseDto {
	
	private List<ReviewRequestDto> userReviews;
	
	private GetReviewResponseDto(List<ReviewRequestDto> userReviews) {
		this.userReviews = userReviews;
	}
	
	public static ResponseEntity<GetReviewResponseDto> success(List<ReviewRequestDto> reviews){
		GetReviewResponseDto body = new GetReviewResponseDto(reviews);
		
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
