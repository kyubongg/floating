package com.floating.mvc.dao;

import com.floating.mvc.dto.request.user.SignupRequestDto;

public interface UserDao {
	int insertUser(SignupRequestDto user);
}
