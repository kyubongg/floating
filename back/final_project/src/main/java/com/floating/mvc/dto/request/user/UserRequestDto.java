package com.floating.mvc.dto.request.user;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
	
	private String id;
	private String email;
	private String pw;
	private String name;
    private Date birth;
    private char gender;
    private int height;
    private int weight;
    private int mbtiPk;
    private String mbtiName;
	
}
