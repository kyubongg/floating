package com.floating.mvc.dto.request.review;

import java.util.List;

import com.floating.mvc.dto.response.review.ImageInfoDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
	private int reviewPk;
	private String content;
	private String completeDate;
	private List<ImageInfoDto> imageUrls;
}
