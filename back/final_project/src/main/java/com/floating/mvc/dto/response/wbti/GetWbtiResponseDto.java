package com.floating.mvc.dto.response.wbti;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.wbti.WbtiRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class GetWbtiResponseDto extends ResponseDto {
	
	private int socialType;
	private int motivationType;
	private int executionType;
	private int activityType;
	
	private GetWbtiResponseDto(WbtiRequestDto dto) {
//		this.socialType = dto.getSocialType();
//		this.motivationType = dto.getMotivationType();
//		this.executionType = dto.getExecutionType();
//		this.activityType = dto.getActivityType();
	}
	
	public static ResponseEntity<GetWbtiResponseDto> success(WbtiRequestDto dto){
		GetWbtiResponseDto body = new GetWbtiResponseDto(dto);
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
