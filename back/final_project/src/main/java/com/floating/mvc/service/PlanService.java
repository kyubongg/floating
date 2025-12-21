package com.floating.mvc.service;

import java.util.Date;

import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanDetailResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanListResponseDto;

public interface PlanService {
	
	// 계획 생성
	ResponseEntity<ResponseDto> insertPlan(PlanRegistRequestDto dto, String userId);
	// 계획 수정
	ResponseEntity<ResponseDto> updatePlan(PlanRequestDto dto, String userId);
	// 계획 전체 조회
	ResponseEntity<? super GetPlanListResponseDto> getPlans(String userId);
	// 계획 상세 조회
	ResponseEntity<? super GetPlanDetailResponseDto> getPlanDetail(int planPk, String userId);
	// 계획 완료
	ResponseEntity<ResponseDto> postPlanCompl(int planPk);
	// 이번주 계획 생성 (지난주 계획 복사)
	ResponseEntity<ResponseDto> insertWeeklyPlan(PlanRegistRequestDto dto, String userId);
	// 내일 계획 생성 (오늘 계획 미루기)
	ResponseEntity<ResponseDto> shiftTodayPlanToTomorrow(String userId);
}
