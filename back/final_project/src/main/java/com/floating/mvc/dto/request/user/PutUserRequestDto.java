package com.floating.mvc.dto.request.user;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutUserRequestDto {
	
	private String id;
	private String pw;
	private String name;
    private Date birth;
    private char gender;
    private int height;
    private int weight;
    
}
