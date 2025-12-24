package com.floating.mvc.service.implement;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.floating.mvc.dao.PetDao;
import com.floating.mvc.dao.UserDao;
import com.floating.mvc.dto.request.user.DeleteUserRequestDto;
import com.floating.mvc.dto.request.user.FindInfoRequestDto;
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
	private final PetDao petDao;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	@Autowired
    private JavaMailSender mailSender;
	
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
			if (result == 1) {
				
				petDao.insertPet(userId);
				return ResponseDto.success(HttpStatus.CREATED);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return null;
	}
	
	@Override
	public ResponseEntity<? super SignInResponseDto> signInUser(SignInRequestDto dto) {
		
		
		UserRequestDto userDto = null;
		String userPw = null;
		String accessToken = null;
		
		try {
			
			String requestId = dto.getId();
			String requestPw = dto.getPw();
			
			userDto = userDao.selectUser(requestId);
			userPw = userDao.selectPasswordById(requestId);
			if(userDto == null)
				return ResponseDto.noExistUser();
			
			if(!passwordEncoder.matches(requestPw, userPw))
				return ResponseDto.validationFail();
			
			accessToken = jwtProvider.create(requestId);
			
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
			System.out.println(dto.getCheerUpQuotes());
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
		    
			int result = userDao.updateUser(dto, userId);
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

	@Override
	public ResponseEntity<ResponseDto> sendCode(FindInfoRequestDto dto) {
		try {
	        // 유저 확인
	        boolean userCount = userDao.existUser(dto);
	        if (!userCount) {
	            return ResponseDto.validationFail();
	        }

	        // 6자리 인증 코드 생성
	        String code = String.format("%06d", new Random().nextInt(1000000));

	        // 메일 발송
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(dto.getEmail());
	        message.setSubject("[Floating] 비밀번호 찾기 인증번호");
	        message.setText("요청하신 인증번호는 [" + code + "] 입니다.\n5분 이내에 입력해주세요.");
	        mailSender.send(message);

	        // 세션 저장을 위한 코드 전달
	        return ResponseDto.success(code); 

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseDto.databaseError();
	    }
	}

	@Override
	public ResponseEntity<ResponseDto> verifyCode(FindInfoRequestDto dto, String savedCode) {
		String userId = null;
		try {
	        if (savedCode == null) {
	            return ResponseDto.validationFail();
	        }

	        if (!savedCode.equals(dto.getCode())) {
	            return ResponseDto.validationFail();
	        }
	        
	        userId = userDao.selectIdByEmail(dto.getEmail());
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseDto.databaseError();
	    }
	    return ResponseDto.success(userId);
	}
	
	@Override
	public ResponseEntity<ResponseDto> resetPassword(FindInfoRequestDto dto) {
	    try {
	        if (dto.getNewPw() == null || dto.getNewPw().trim().isEmpty()) {
	            return ResponseDto.validationFail();
	        }

			// 비밀번호 암호화
			String encodedPassword = passwordEncoder.encode(dto.getNewPw());
			dto.setNewPw(encodedPassword);
			
			int result = userDao.updatePassword(dto);
	        if (result == 0) {
	            return ResponseDto.databaseError();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseDto.databaseError();
	    }

	    return ResponseDto.success(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseDto> updateUserQuotes(List<String> quotes, String userId) {
		
		try {
	        
			int result = userDao.updateUserQuotes(quotes, userId);
			if (result == 0) {
	            return ResponseDto.databaseError();
	        }
			
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseDto.databaseError();
	    }

	    return ResponseDto.success(HttpStatus.OK);
	}
}
