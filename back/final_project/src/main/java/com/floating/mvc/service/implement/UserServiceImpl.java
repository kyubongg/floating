package com.floating.mvc.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.floating.mvc.dao.UserDao;
import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserDao userDao;
	
	@Override
	public ResponseEntity<ResponseDto> insertUser(SignupRequestDto user) {
		
		try {
			// 아이디 중복 검사
			String userId = user.getId();
			boolean existUserId = userDao.existById(userId);
			if (existUserId)
				return ResponseDto.existUserId();
			
			// 이메일 중복 검사
			String userEmail = user.getEmail();
			boolean existUserEmail = userDao.existByEmail(userEmail);
			if (existUserEmail)
				return ResponseDto.existUserEmail();
			
			int result = userDao.insertUser(user);
			
			if (result == 1) {
				return ResponseDto.success(HttpStatus.CREATED);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return null;
	}
	
}
