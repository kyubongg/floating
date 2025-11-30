package com.floating.mvc.service;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.response.ResponseDto;

public interface UserService {
	ResponseEntity<ResponseDto> insertUser(User user);
}
