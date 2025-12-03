package com.floating.mvc.service.implement;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.floating.mvc.dao.PlanDao;
import com.floating.mvc.dto.request.plan.GetPlanDetailRequestDto;
import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanDetailResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanListResponseDto;
import com.floating.mvc.service.PlanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
	
	private final PlanDao planDao;

	@Override
	public ResponseEntity<ResponseDto> insertPlan(PlanRegistRequestDto dto, String userId) {
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
			PlanRequestDto plan = planDao.selectPlan(dto.getPlanPk(), userId);
		
			
			int result = planDao.updatePlan(dto);
			
			
			if(result == 0) return ResponseDto.databaseError();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<? super GetPlanListResponseDto> getPlans(String userId) {
		
		List<PlanRequestDto> planList = new ArrayList<>(); 
		
		try {
			
			planList = planDao.selectAll(userId);
			
			System.out.println(planList);
			if(planList.size() == 0) return ResponseDto.noExistPlan();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return GetPlanListResponseDto.success(planList);
	}

	@Override
	public ResponseEntity<? super GetPlanDetailResponseDto> getPlanDetail(int planPk, String userId) {
		
		PlanRequestDto plan = null;
		
		try {
			
			plan = planDao.selectPlan(planPk, userId); 
			
			if(plan == null) return ResponseDto.noExistPlan();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return GetPlanDetailResponseDto.success(plan);
	}

	@Override
	public ResponseEntity<ResponseDto> postPlanCompl(int planPk) {

		try {
			
			LocalDate localDate = LocalDate.now();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			String formattedDate = localDate.format(formatter);
			
			int result = planDao.postPlanCompl(planPk, formattedDate);
			
			if(result == 0) return ResponseDto.noExistPlan();
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}



}
