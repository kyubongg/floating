package com.floating.mvc.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {
	
	private String code;
	private String message;
	
	public ResponseDto() {
		this.code = ResponseCode.SUCCESS;
		this.message = ResponseMessage.SUCCESS;
	}

	public static ResponseEntity<ResponseDto> success(HttpStatus status){
		ResponseDto body = new ResponseDto();
		return ResponseEntity.status(status).body(body);
	}
	
	
	// ============================================== //
    // HTTP/1.1 400 Bad Request
	
	public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto body = new ResponseDto(ResponseCode.NO_EXIST_USER, ResponseMessage.NO_EXIST_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
	
	public static ResponseEntity<ResponseDto> existUserId() {
		ResponseDto body = new ResponseDto(ResponseCode.EXIST_USER_ID, ResponseMessage.EXIST_USER_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
	
	public static ResponseEntity<ResponseDto> existUserEmail() {
		ResponseDto body = new ResponseDto(ResponseCode.EXIST_USER_EMAIL, ResponseMessage.EXIST_USER_EMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
	
	public static ResponseEntity<ResponseDto> validationFail() {
		ResponseDto body = new ResponseDto(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
	
	public static ResponseEntity<ResponseDto> noExistPlan() {
        ResponseDto body = new ResponseDto(ResponseCode.NO_EXIST_PLAN, ResponseMessage.NO_EXIST_PLAN);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
	
	public static ResponseEntity<ResponseDto> noExistReview() {
        ResponseDto body = new ResponseDto(ResponseCode.NO_EXIST_REVIEW, ResponseMessage.NO_EXIST_REVIEW);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
	
	public static ResponseEntity<ResponseDto> noExistWbti() {
        ResponseDto body = new ResponseDto(ResponseCode.NO_EXIST_WBTI, ResponseMessage.NO_EXIST_WBTI);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
	
	// ============================================== //
    // HTTP/1.1 500 Internal Server Error
	
	public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto body = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
	
}
