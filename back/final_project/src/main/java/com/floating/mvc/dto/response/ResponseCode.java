package com.floating.mvc.dto.response;

public interface ResponseCode {

	// HTTP/1.1 200 OK
	String SUCCESS = "SU";
	
	// ============================================== //
    // HTTP/1.1 400 Bad Request
	String NO_EXIST_USER = "NU";
	
	// ============================================== //
	// HTTP/1.1 500 Internal Server Error
    String DATABASE_ERROR = "DBE";
}
