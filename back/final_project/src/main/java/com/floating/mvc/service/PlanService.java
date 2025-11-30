package com.floating.mvc.service;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

public interface PlanService {
	
	ResponseEntity<ResponseDto> insertPlan(PlanRequestDto dto);
}
