package com.floating.mvc.dao;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

public interface UserDao {
	int insertUser(User user);
}
