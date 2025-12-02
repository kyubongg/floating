package com.floating.mvc.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.floating.mvc.dao.PlanDao;
import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.service.PlanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
	
	private final PlanDao planDao;

	@Override
	public ResponseEntity<ResponseDto> insertPlan(PlanRequestDto dto, String userId) {
		try {
			// AI API 호출 
		}
		catch (Exception e) {
			
		}
		
		return null;
	}

	@Override
	public ResponseEntity<ResponseDto> updatePlan(PlanRequestDto dto, String userId) {
		
		try {
			
			// userId를 이용해 계획을 조회
			PlanRequestDto plan = planDao.selectPlan(userId, dto.getDate());
			
			int result = planDao.updatePlan(plan, userId);
			
			if(result == 0) return ResponseDto.databaseError();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}
	

}
