package com.floating.mvc.dao;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;

public interface PlanDao {
	// 계획 작성
	int insertPlan(
			@Param("plan") PlanRegistRequestDto plan,   // DTO를 'plan'이라는 이름으로 바인딩
	        @Param("userId") String userId);
	// 계획 수정
	int updatePlan(PlanRequestDto plan);      
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

}
