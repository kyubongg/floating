package com.floating.mvc.dto.response;

public interface ResponseMessage {

	// HTTP/1.1 200 OK
	String SUCCESS = "Success";
	
	// ============================================== //
    // HTTP/1.1 400 Bad Request
	String NO_EXIST_USER = "No Exist User";
	
	String NO_EXIST_PLAN = "존재하지 않는 계획입니다.";
	
	// ============================================== //
    // HTTP/1.1 500 Internal Server Error
    String DATABASE_ERROR = "DataBase Error";
}
