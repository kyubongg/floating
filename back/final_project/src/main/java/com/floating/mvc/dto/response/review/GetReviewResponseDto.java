package com.floating.mvc.dto.response.review;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.response.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewResponseDto extends ResponseDto {
	
	private int reviewPk;
	private String content;
	private List<ImageInfoDto> imageUrls;
	
	
	public static ResponseEntity<GetReviewResponseDto> success(GetReviewResponseDto dto){
		GetReviewResponseDto body = new GetReviewResponseDto(
			dto.getReviewPk(),
			dto.getContent(),
			dto.getImageUrls()
		);
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
