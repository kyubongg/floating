package com.floating.mvc.dto.response;

public interface ResponseMessage {

	// HTTP/1.1 200 OK
	String SUCCESS = "Success";
	
	// ============================================== //
    // HTTP/1.1 400 Bad Request
	String NO_EXIST_USER = "존재하지 않는 유저입니다.";
    String EXIST_USER_ID = "이미 존재하는 아이디입니다.";
    String EXIST_USER_EMAIL = "이미 존재하는 이메일입니다.";
	
	String NO_EXIST_PLAN = "존재하지 않는 계획입니다.";
	String NO_EXIST_PLANLIST = "계획이 하나도 없습니다.";
	
	String NO_EXIST_REVIEW = "존재하지 않는 리뷰입니다.";
	// ============================================== //
    // HTTP/1.1 500 Internal Server Error
    String DATABASE_ERROR = "DataBase Error";
    
}
