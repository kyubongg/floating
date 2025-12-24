package com.floating.mvc.dto.request.user;

import java.sql.Date;
import java.util.Map;

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
    private String wbtiCode;
    private int petScore;
    private Map<String, Object> cheerUpQuotes;
}
