package com.floating.mvc.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.floating.mvc.dao.UserDao;
import com.floating.mvc.dto.request.user.DeleteUserRequestDto;
import com.floating.mvc.dto.request.user.PutUserMbtiRequestDto;
import com.floating.mvc.dto.request.user.PutUserRequestDto;
import com.floating.mvc.dto.request.user.SignInRequestDto;
import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.request.user.UserRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.user.GetUserDetailResponseDto;
import com.floating.mvc.dto.response.user.SignInResponseDto;
import com.floating.mvc.provider.JwtProvider;
import com.floating.mvc.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserDao userDao;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	
	@Override
	public ResponseEntity<ResponseDto> signUpUser(SignupRequestDto dto) {
		
		try {
			// 아이디 중복 검사
			String userId = dto.getId();
			boolean existUserId = userDao.existById(userId);
			if (existUserId)
				return ResponseDto.existUserId();
			
			// 이메일 중복 검사
			String userEmail = dto.getEmail();
			boolean existUserEmail = userDao.existByEmail(userEmail);
			if (existUserEmail)
				return ResponseDto.existUserEmail();
			
			// 비밀번호 암호화
			String encodedPassword = passwordEncoder.encode(dto.getPw());
			dto.setPw(encodedPassword);
			
			int result = userDao.insertUser(dto);
			if (result == 1) 
				return ResponseDto.success(HttpStatus.CREATED);

		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return null;
	}
	
	@Override
	public ResponseEntity<? super SignInResponseDto> signInUser(SignInRequestDto dto) {
		
		
		UserRequestDto userDto = null;
		String accessToken = null;
		
		try {
			
			String userId = dto.getId();
			String userPw = dto.getPw();
			
			userDto = userDao.selectUser(userId);
			if(userDto == null)
				return ResponseDto.noExistUser();
			
			if(!passwordEncoder.matches(userPw, userDto.getPw()))
				return ResponseDto.validationFail();
			
			accessToken = jwtProvider.create(userId);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return SignInResponseDto.success(userDto, accessToken);
	}

	@Override
	public ResponseEntity<? super GetUserDetailResponseDto> getUserDetail (String userId) {

		UserRequestDto dto = null;
		
		try {
			
			dto = userDao.selectUser(userId);
			if(dto == null) 
				return ResponseDto.noExistUser();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return GetUserDetailResponseDto.success(dto);
	}

	@Override
	public ResponseEntity<ResponseDto> updateUser(PutUserRequestDto dto, 
			String userId) {
		try {
			// 비밀번호 확인
		    boolean isPasswordVaild = verifyPassword(userId, dto.getPw());
		    if (!isPasswordVaild)
		    	return ResponseDto.validationFail();
		    
		    // 비밀번호 암호화
		    String encodedPassword = passwordEncoder.encode(dto.getPw());
		    dto.setPw(encodedPassword);
		    
			int result = userDao.updateUser(dto);
			if(result == 0) 
				return ResponseDto.databaseError();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseDto> updateUserMbti(PutUserMbtiRequestDto dto, 
			String userId) {
		try {
			
			int result = userDao.updateUserMbti(dto);
			if(result == 0) 
				return ResponseDto.databaseError();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseDto> deleteUser(DeleteUserRequestDto dto, String userId) {

		try {
			// 비밀번호 확인
		    boolean isPasswordVaild = verifyPassword(userId, dto.getPw());
		    if (!isPasswordVaild)
		    	return ResponseDto.validationFail();

			int result = userDao.deleteUser(userId);
			if (result == 0) 
				return ResponseDto.databaseError();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}
	
    public boolean verifyPassword(String userId, String rawPassword) {
        String encodedPassword = userDao.selectPasswordById(userId);
        
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
