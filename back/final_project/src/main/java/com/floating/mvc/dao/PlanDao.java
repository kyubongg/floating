package com.floating.mvc.dao;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.floating.mvc.dto.request.plan.PlanRequestDto;

public interface PlanDao {
	// 계획 작성
	int insertPlan(PlanRequestDto plan);
	// 계획 수정
	int updatePlan(
	        @Param("plan") PlanRequestDto plan,   // DTO를 'plan'이라는 이름으로 바인딩
	        @Param("userId") String userId);      // userId를 'userId'라는 이름으로 바인딩
	// 계획 전체 조회
	List<PlanRequestDto> selectAll();
	// 계획 상세 조회
	PlanRequestDto selectPlan(
			@Param("userId")String userId, 
			@Param("planDate") Date date);
	

}
