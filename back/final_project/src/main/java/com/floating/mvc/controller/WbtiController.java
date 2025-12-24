package com.floating.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floating.mvc.dto.request.wbti.PostWbtiRequestDto;
import com.floating.mvc.dto.request.wbti.PutWbtiRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.wbti.GetWbtiResponseDto;
import com.floating.mvc.service.WbtiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wbti")
public class WbtiController {

	private final WbtiService wbtiService;
	
	@PostMapping("/")
	public ResponseEntity<ResponseDto> insertWbti(
			@RequestBody PostWbtiRequestDto dto,
			@AuthenticationPrincipal String userId
	){
		ResponseEntity<ResponseDto> response = wbtiService.insertWbti(dto, userId);
		return response;
	}
	
	@GetMapping("/")
	public ResponseEntity<? super GetWbtiResponseDto> getWbtiResult(
			@AuthenticationPrincipal String userId
	){
		ResponseEntity<? super GetWbtiResponseDto> response = wbtiService.getWbtiResult(userId);
		return response;
	}
	
	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateWbti(
			@RequestBody PutWbtiRequestDto dto,
			@AuthenticationPrincipal String userId
	){
		ResponseEntity<ResponseDto> response = wbtiService.updateWbti(dto, userId);
		return response;
	}
}
