package com.floating.mvc.dao;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;

public interface PlanDao {
	// 계획 작성
	int insertPlan(
			@Param("plan") PlanRegistRequestDto dto,  
	        @Param("userId") String userId);
	int insertPlanBatch(
			@Param("plans") List<PlanRequestDto> plans);
	// 계획 수정
	int updatePlan(PlanRequestDto dto);      
	// 계획 전체 조회
	List<PlanRequestDto> selectAll(String userId);
	// 계획 상세 조회
	PlanRequestDto selectPlan(
			@Param("planPk") int planPk,
			@Param("userId") String userId);
	// 계획 완료
	int postPlanCompl(
			@Param("planPk") int planPk, 
			@Param("completeDate") String formattedDate);
	// 계획 완료 취소
	int postPlanUncompl(
			@Param("planPk") int planPk);
	// 이번주 계획 생성 (지난주 계획 복사)
	int postponeWeeklyPlan(
			@Param("userId") String userId);
	// 내일 계획 생성 (오늘 계획 복사)
	int insertTomorrowPlan(
			@Param("userId") String userId);
	// 오늘 계획 삭제
	int deleteTodayPlan(
			@Param("userId") String userId);
	
}
