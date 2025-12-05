package com.floating.mvc.service.implement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.floating.mvc.dao.PlanDao;
import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanDetailResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanListResponseDto;
import com.floating.mvc.event.PlanCompleteEvent;
import com.floating.mvc.service.PlanService;
import com.floating.mvc.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
	
	private final PlanDao planDao;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	public ResponseEntity<ResponseDto> insertPlan(PlanRegistRequestDto dto, String userId) {
		
		int planPk = 0;
		try {
			// AI API 호출
			planDao.insertPlan(dto, userId);
			
			if(planPk == 0) return ResponseDto.databaseError();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.CREATED);
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
		
		// PlanEntity <= 테이블에 있는 모든 컬럼을 가지고 있는 클래스
		PlanRequestDto plan = null;
		
		// 그리고 유지보수 쉽게 하기 위해서
		//  
		
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
	@Transactional
	public ResponseEntity<ResponseDto> postPlanCompl(int planPk) {

		try {
			
			LocalDate localDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = localDate.format(formatter);
			
			int result = planDao.postPlanCompl(planPk, formattedDate);
			
			if(result == 0) return ResponseDto.noExistPlan();
			
			eventPublisher.publishEvent(new PlanCompleteEvent(planPk));
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

}


@Component
@RequiredArgsConstructor
class PlanEventListener {
	
	private final ReviewService reviewService;
	
	@Async
	@TransactionalEventListener
	public void handlePlanCompl(PlanCompleteEvent event) {
		
		reviewService.insertReview(event.getPlanPk());
	}
}
