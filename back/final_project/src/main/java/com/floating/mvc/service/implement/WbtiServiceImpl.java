package com.floating.mvc.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.floating.mvc.dto.request.wbti.PostWbtiRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.service.WbtiService;

@Service
public class WbtiServiceImpl implements WbtiService {

	@Override
	public ResponseEntity insertWbti(PostWbtiRequestDto dto, String userId) {
		
		try {
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return null;
	}

}
