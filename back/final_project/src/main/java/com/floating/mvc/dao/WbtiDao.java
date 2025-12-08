package com.floating.mvc.dao;

import com.floating.mvc.dto.request.wbti.PostWbtiRequestDto;
import com.floating.mvc.dto.request.wbti.PutWbtiRequestDto;
import com.floating.mvc.dto.request.wbti.WbtiRequestDto;

public interface WbtiDao {
	// wbti 검사 결과 등록
	int insertWbti(PostWbtiRequestDto dto);
	// 유저 wbti 조회
	WbtiRequestDto selectByUserId(String userId);
	// 유저 wbti 수정
	int updateByUserId(PutWbtiRequestDto dto);
}
