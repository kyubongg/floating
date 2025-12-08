package com.floating.mvc.service;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.wbti.PostWbtiRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

public interface WbtiService {
	// wbti 검사 결과 등록
	ResponseEntity<ResponseDto> insertWbti(PostWbtiRequestDto dto, String userId);
}
