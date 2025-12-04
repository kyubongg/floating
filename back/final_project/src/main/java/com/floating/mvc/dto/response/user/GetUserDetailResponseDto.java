package com.floating.mvc.dto.response.user;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.user.UserRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserDetailResponseDto extends ResponseDto {
	
	private String id;
	private String email;
	private String pw;
	private String name;
    private Date birth;
    private char gender;
    private int height;
    private int weight;
    private String mbtiName;
    
    private GetUserDetailResponseDto(UserRequestDto dto) {
    	this.id = dto.getId();
    	this.email = dto.getEmail();
    	this.pw = dto.getPw();
    	this.name = dto.getName();
    	this.birth = dto.getBirth();
    	this.gender = dto.getGender();
    	this.height = dto.getHeight();
    	this.weight = dto.getWeight();
    	this.mbtiName = dto.getMbtiName();
    }
    
	public static ResponseEntity<GetUserDetailResponseDto> success(UserRequestDto dto){
		
		GetUserDetailResponseDto body = new GetUserDetailResponseDto(dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
