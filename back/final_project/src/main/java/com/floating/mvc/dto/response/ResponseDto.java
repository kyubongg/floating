package com.floating.mvc.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
	
	private String code = ResponseCode.SUCCESS;
	private String message = ResponseMessage.SUCCESS;

	public static ResponseEntity<ResponseDto> success(HttpStatus status){
		ResponseDto body = new ResponseDto();
		return ResponseEntity.status(status).body(body);
	}
	
	public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto body = new ResponseDto(ResponseCode.NO_EXIST_USER, ResponseMessage.NO_EXIST_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
