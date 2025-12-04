package com.floating.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floating.mvc.dto.request.user.PutUserMbtiRequestDto;
import com.floating.mvc.dto.request.user.PutUserRequestDto;
import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.user.GetUserDetailResponseDto;
import com.floating.mvc.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<ResponseDto> signUpUser(
			@RequestBody SignupRequestDto dto){
		
		ResponseEntity<ResponseDto> response = userService.signUpUser(dto);
		return response;
	}
	
	@GetMapping("/detail")
	public ResponseEntity<? super GetUserDetailResponseDto> getUser(String userId){
		
		ResponseEntity<? super GetUserDetailResponseDto> response = userService.getUserDetail(userId);
		return response;
	}
	
	@PostMapping("/")
	public ResponseEntity<ResponseDto> updateUser(
			@RequestBody PutUserRequestDto dto,
			String userId){
		
		ResponseEntity<ResponseDto> response = userService.updateUser(dto, userId);
		return response;
	}
	
	@PostMapping("/mbti")
	public ResponseEntity<ResponseDto> updateUserMbti(
			@RequestBody PutUserMbtiRequestDto dto,
			String userId){
		
		ResponseEntity<ResponseDto> response = userService.updateUserMbti(dto, userId);
		return response;
	}
	
}
