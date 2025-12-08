package com.floating.mvc.dto.response;

public interface ResponseCode {

	// HTTP/1.1 200 OK
	String SUCCESS = "SU";
	
	// ============================================== //
    // HTTP/1.1 400 Bad Request
	String NO_EXIST_USER = "NU";
    String EXIST_USER_ID = "EID";
    String EXIST_USER_EMAIL = "EEM";
    String VALIDATION_FAIL = "VF";
	
	String NO_EXIST_PLAN = "NP";
	String NO_EXIST_PLANLIST = "NPL";
	
	String NO_EXIST_REVIEW = "NR";
	
	String NO_EXIST_WBTI = "NW";
	
	// ============================================== //
	// HTTP/1.1 500 Internal Server Error
    String DATABASE_ERROR = "DBE";

}
