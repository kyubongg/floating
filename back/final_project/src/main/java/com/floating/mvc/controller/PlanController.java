package com.floating.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanDetailResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanListResponseDto;
import com.floating.mvc.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class PlanController {
	
	private final PlanService planService;

	@GetMapping("/")
	public ResponseEntity<? super GetPlanListResponseDto> getPlanList(
			@AuthenticationPrincipal String userId
	){
		ResponseEntity<? super GetPlanListResponseDto> response = planService.getPlans(userId);
		return response;
	}
	
	@GetMapping("/detail/{planPk}")
	public ResponseEntity<? super GetPlanDetailResponseDto> getPlanDetail(
			@PathVariable int planPk,
			@AuthenticationPrincipal String userId
	){
		ResponseEntity<? super GetPlanDetailResponseDto> response = planService.getPlanDetail(planPk, userId);
		return response;
	}
	
	@PostMapping("/complete/{planPk}")
	public ResponseEntity<ResponseDto> postPlanCompl(
			@PathVariable int planPk,
			@AuthenticationPrincipal String userId
	){
		ResponseEntity<ResponseDto> response = planService.postPlanCompl(planPk, userId);
		return response;
	}
	
	@PostMapping("/uncomplete/{planPk}")
	public ResponseEntity<ResponseDto> postPlanUncompl(
			@PathVariable int planPk,
			@AuthenticationPrincipal String userId
	){
		ResponseEntity<ResponseDto> response = planService.postPlanUncompl(planPk, userId);
		return response;
	}
	
	@PutMapping("/")
	public ResponseEntity<ResponseDto> updatePlan(
		@RequestBody PlanRequestDto dto,
		@AuthenticationPrincipal String userId
	){
		ResponseEntity<ResponseDto> response = planService.updatePlan(dto, userId);
		return response;
	}
	
	@PostMapping("/weekly")
	public ResponseEntity<ResponseDto> insertWeeklyPlan(
		@RequestBody PlanRegistRequestDto dto,
		@AuthenticationPrincipal String userId
	){
		ResponseEntity<ResponseDto> response = planService.insertWeeklyPlan(dto, userId);
		return response;
	}
	
	@PostMapping("/today")
	public ResponseEntity<ResponseDto> shiftTodayPlanToTomorrow(
			@AuthenticationPrincipal String userId
	){
		ResponseEntity<ResponseDto> response = planService.shiftTodayPlanToTomorrow(userId);
		return response;
	}
	
}
