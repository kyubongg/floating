package com.floating.mvc.dao;

import com.floating.mvc.dto.request.user.PutUserMbtiRequestDto;
import com.floating.mvc.dto.request.user.PutUserRequestDto;
import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.request.user.UserRequestDto;

public interface UserDao {
	
	//회원가입 
	int insertUser(SignupRequestDto user);
	
	//중복 체크
    boolean existById(String id);
    boolean existByEmail(String email);
    
    //회원 상세 조회
    UserRequestDto selectUser(String userId);
    
    //회원 정보 수정 
    int updateUser(PutUserRequestDto dto); 
    
    //회원 Mbti 정보 수정
    int updateUserMbti(PutUserMbtiRequestDto dto);
    
}
