package com.floating.mvc.service;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.wbti.PostWbtiRequestDto;
import com.floating.mvc.dto.request.wbti.PutWbtiRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.wbti.GetWbtiResponseDto;

public interface WbtiService {
	// wbti 검사 결과 등록
	ResponseEntity<ResponseDto> insertWbti(PostWbtiRequestDto dto, String userId);
	// 유저 wbti 조회
	ResponseEntity<? super GetWbtiResponseDto> getWbtiResult(String userId);
	// 유저 wbti 수정
	ResponseEntity<ResponseDto> updateWbti(PutWbtiRequestDto dto, String userId);
}
