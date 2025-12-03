package com.floating.mvc.dao;

import com.floating.mvc.dto.request.user.SignupRequestDto;

public interface UserDao {
	
	//회원가입 
	public int insertUser(SignupRequestDto user);
	
	// 중복 체크
    boolean existById(String id);
    boolean existByEmail(String email);
}
