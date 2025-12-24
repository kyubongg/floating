package com.floating.mvc.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.user.DeleteUserRequestDto;
import com.floating.mvc.dto.request.user.FindInfoRequestDto;
import com.floating.mvc.dto.request.user.PutUserMbtiRequestDto;
import com.floating.mvc.dto.request.user.PutUserRequestDto;
import com.floating.mvc.dto.request.user.SignInRequestDto;
import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.user.GetUserDetailResponseDto;
import com.floating.mvc.dto.response.user.SignInResponseDto;


public interface UserService {
	
	// 회원가입
	ResponseEntity<ResponseDto> signUpUser(SignupRequestDto dto);
	// 로그인
	ResponseEntity<? super SignInResponseDto> signInUser(SignInRequestDto dto);
	// 회원 상세 조회
	ResponseEntity<? super GetUserDetailResponseDto> getUserDetail (String userId);
	// 회원 정보 수정
	ResponseEntity<ResponseDto> updateUser(PutUserRequestDto dto, String userId);
	// 회원 응원 문구 수정
	ResponseEntity<ResponseDto> updateUserQuotes(List<String> quotes, String userId);
	// 회원 Mbti 수정
	ResponseEntity<ResponseDto> updateUserMbti(PutUserMbtiRequestDto dto, String userId);
	// 회원탈퇴
	ResponseEntity<ResponseDto> deleteUser(DeleteUserRequestDto dto, String userId);
	// 이메일 인증 코드 전송
	ResponseEntity<ResponseDto> sendCode(FindInfoRequestDto dto);
	// 이메일 인증 코드 검증
	ResponseEntity<ResponseDto> verifyCode(FindInfoRequestDto dto, String savedCode);
	// 비밀번호 재설정
	ResponseEntity<ResponseDto> resetPassword(FindInfoRequestDto dto);
}
