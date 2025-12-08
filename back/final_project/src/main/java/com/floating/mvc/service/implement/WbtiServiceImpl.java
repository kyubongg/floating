package com.floating.mvc.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.floating.mvc.dao.WbtiDao;
import com.floating.mvc.dto.request.wbti.PostWbtiRequestDto;
import com.floating.mvc.dto.request.wbti.PutWbtiRequestDto;
import com.floating.mvc.dto.request.wbti.WbtiRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.wbti.GetWbtiResponseDto;
import com.floating.mvc.service.WbtiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WbtiServiceImpl implements WbtiService {

	private final WbtiDao wbtiDao;
	
	@Override
	public ResponseEntity<ResponseDto> insertWbti(PostWbtiRequestDto dto, String userId) {
		
		try {
			
			int result = wbtiDao.insertWbti(dto);
			if(result == 0) 
				return ResponseDto.databaseError();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<? super GetWbtiResponseDto> selectByUserId(String userId) {
		
		WbtiRequestDto body = null;
		try {
			
			body = wbtiDao.selectByUserId(userId);
			if(body == null)
				return ResponseDto.noExistWbti();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		
		return GetWbtiResponseDto.success(body);
	}

	@Override
	public ResponseEntity<ResponseDto> updateWbti(PutWbtiRequestDto dto, String userId) {
		
		try {
			
			dto.setUserId(userId);
			
			int result = wbtiDao.updateByUserId(dto);
			if(result == 0)
				return ResponseDto.databaseError();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

}
