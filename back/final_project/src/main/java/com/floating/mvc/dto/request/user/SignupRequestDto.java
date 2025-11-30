package com.floating.mvc.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
	
	private String id;
	private String email;
	private String pw;
	private String name;
    private int age;
    private char gender;
    private int height;
    private int weight;
	
}

