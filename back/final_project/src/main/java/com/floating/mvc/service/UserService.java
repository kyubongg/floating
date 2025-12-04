package com.floating.mvc.service;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.request.user.UserRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.user.GetUserDetailResponseDto;

public interface UserService {
	
	// 회원가입
	ResponseEntity<ResponseDto> signUpUser(SignupRequestDto dto);
	// 회원 상세 조회
	ResponseEntity<? super GetUserDetailResponseDto> getUserDetail (String userId);
}
