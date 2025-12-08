package com.floating.mvc.dao;

import com.floating.mvc.dto.request.wbti.PostWbtiRequestDto;

public interface WbtiDao {
	// wbti 검사 결과 등록
	int insertWbti(PostWbtiRequestDto dto);
}
