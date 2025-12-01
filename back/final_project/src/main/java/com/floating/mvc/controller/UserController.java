package com.floating.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	

	
	@PostMapping("/signup")
	public ResponseEntity<ResponseDto> insertUser(
						@RequestBody SignupRequestDto dto){
		ResponseEntity<ResponseDto> response = userService.insertUser(dto);
		
		return response;
	}
	
}
