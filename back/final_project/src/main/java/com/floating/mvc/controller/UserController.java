package com.floating.mvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floating.mvc.dto.request.user.DeleteUserRequestDto;
import com.floating.mvc.dto.request.user.FindInfoRequestDto;
import com.floating.mvc.dto.request.user.PutUserMbtiRequestDto;
import com.floating.mvc.dto.request.user.PutUserRequestDto;
import com.floating.mvc.dto.request.user.SignInRequestDto;
import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.user.GetUserDetailResponseDto;
import com.floating.mvc.dto.response.user.SignInResponseDto;
import com.floating.mvc.service.UserService;

import jakarta.servlet.http.HttpSession;
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
	
	@PostMapping("/signin")
	public ResponseEntity<? super SignInResponseDto> signInUser(
			@RequestBody SignInRequestDto dto
	){
		ResponseEntity<? super SignInResponseDto> response = userService.signInUser(dto);
		return response;
	}
	
	
	@GetMapping("/detail")
	public ResponseEntity<? super GetUserDetailResponseDto> getUser(
			@AuthenticationPrincipal String userId){
		
		ResponseEntity<? super GetUserDetailResponseDto> response = userService.getUserDetail(userId);
		return response;
	}
	
	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateUser(
			@RequestBody PutUserRequestDto dto,
			@AuthenticationPrincipal String userId){
		
		ResponseEntity<ResponseDto> response = userService.updateUser(dto, userId);
		return response;
	}
	
	@PostMapping("/mbti")
	public ResponseEntity<ResponseDto> updateUserMbti(
			@RequestBody PutUserMbtiRequestDto dto,
			@AuthenticationPrincipal String userId){
		
		ResponseEntity<ResponseDto> response = userService.updateUserMbti(dto, userId);
		return response;
	}
	
	@DeleteMapping("/")
	public ResponseEntity<ResponseDto> deleteUser(
			@RequestBody DeleteUserRequestDto dto,
			@AuthenticationPrincipal String userId){
		
		ResponseEntity<ResponseDto> response = userService.deleteUser(dto, userId);
		return response;
	}
	
	@PostMapping("/send-code")
	public ResponseEntity<ResponseDto> sendCode(
			@RequestBody FindInfoRequestDto dto,
			HttpSession session){
		
		// 이메일 인증 코드 생성
	    ResponseEntity<ResponseDto> response = userService.sendCode(dto);
	    if (response.getStatusCode() == HttpStatus.OK) {

	        String generatedCode = (String) response.getBody().getData();
	        // 세션 저장
	        String sessionKey = "AUTH_CODE_" + dto.getEmail();
	        session.setAttribute(sessionKey, generatedCode);
	        session.setMaxInactiveInterval(300); // 5분 유효 시간
	        
	        System.out.println("저장 시 세션 ID: " + session.getId());
	        System.out.println("저장 키: " + sessionKey + " | 저장 값: " + generatedCode);
	        
	        return ResponseDto.success(HttpStatus.OK);
	    }

	    return response;
	}
	
	@PostMapping("/verify-code")
	public ResponseEntity<ResponseDto> verifyCode(
			@RequestBody FindInfoRequestDto dto,
			HttpSession session){
		
		// 세션 저장된 이메일 인증 코드 전달
		String sessionKey = "AUTH_CODE_" + dto.getEmail();
	    String savedCode = (String) session.getAttribute(sessionKey);
	    System.out.println("불러오기 시 세션 ID: " + session.getId());
	    System.out.println("불러오기 키: " + sessionKey + " | 불러온 값: " + savedCode);
		ResponseEntity<ResponseDto> response = userService.verifyCode(dto, savedCode);

		
		if (response.getStatusCode() == HttpStatus.OK) {
	        session.setAttribute("VERIFIED_EMAIL", dto.getEmail());
	    }
		return response;
	}
	
	@PutMapping("/password")
	public ResponseEntity<ResponseDto> resetPassword(
	        @RequestBody FindInfoRequestDto dto, 
	        HttpSession session) {
	    
	    // 세션에서 인증 성공 여부 확인
	    String verifiedEmail = (String) session.getAttribute("VERIFIED_EMAIL");
	    if (verifiedEmail == null || !verifiedEmail.equals(dto.getEmail())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                             .body(new ResponseDto("인증되지 않은 접근입니다.", verifiedEmail)); 
	    }
	    
	    dto.setEmail(verifiedEmail);
	    ResponseEntity<ResponseDto> response = userService.resetPassword(dto);
	    
	    // 성공 시 세션에서 인증 성공 여부 삭제
	    if (response.getStatusCode() == HttpStatus.OK) {
	        session.removeAttribute("VERIFIED_EMAIL");
	    }
	    return response;
	}
	
}
