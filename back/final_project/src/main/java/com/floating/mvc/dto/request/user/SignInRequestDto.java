package com.floating.mvc.dto.request.user;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {
	
	private String id;
	private String pw;
	
}
