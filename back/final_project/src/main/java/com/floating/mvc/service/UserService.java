package com.floating.mvc.service;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.user.PutUserMbtiRequestDto;
import com.floating.mvc.dto.request.user.PutUserRequestDto;
import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.user.GetUserDetailResponseDto;

public interface UserService {
	
	// 회원가입
	ResponseEntity<ResponseDto> signUpUser(SignupRequestDto dto);
	// 회원 상세 조회
	ResponseEntity<? super GetUserDetailResponseDto> getUserDetail (String userId);
	// 회원 정보 수정
	ResponseEntity<ResponseDto> updateUser(PutUserRequestDto dto, String userId);
	// 회원 Mbti 수정
	ResponseEntity<ResponseDto> updateUserMbti(PutUserMbtiRequestDto dto, String userId);
}
