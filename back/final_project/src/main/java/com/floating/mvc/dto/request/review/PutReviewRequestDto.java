package com.floating.mvc.dto.request.review;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.floating.mvc.dto.response.review.ImageInfoDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutReviewRequestDto {
	private int reviewPk;
	private String content;
	private List<ImageInfoDto> imgPaths = new ArrayList<>();
	
	// 프론트에서 보낸 이미지 파일(multipart/form-data 전송 시 필요)
	private List<MultipartFile> images;
}
