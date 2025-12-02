package com.floating.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class PlanController {
	
	private final PlanService planService;

	@PutMapping("/")
	public ResponseEntity<ResponseDto> updatePlan(
		@RequestBody PlanRequestDto dto,
		String userId
	){
		ResponseEntity<ResponseDto> response = planService.updatePlan(dto, userId);
		return response;
	}
	
}
