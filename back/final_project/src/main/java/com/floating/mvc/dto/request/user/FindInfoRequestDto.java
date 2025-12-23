package com.floating.mvc.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindInfoRequestDto {
	private String name;
	private String email;
	private String code; 
	private String newPw;
}
