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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.floating.mvc.dao.PetDao;
import com.floating.mvc.dao.PlanDao;
import com.floating.mvc.dao.UserDao;
import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.request.review.PutReviewRequestDto;
import com.floating.mvc.dto.response.ResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanDetailResponseDto;
import com.floating.mvc.dto.response.plan.GetPlanListResponseDto;
import com.floating.mvc.event.PlanCompleteEvent;
import com.floating.mvc.event.PlanUncompleteEvent;
import com.floating.mvc.service.PlanService;
import com.floating.mvc.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
	
	private final PlanDao planDao;
	private final UserDao userDao;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	public ResponseEntity<ResponseDto> insertPlan(PlanRegistRequestDto dto, String userId) {
		
		try {
	
			List<PlanRequestDto> planList = dto.getPlans();
			
			if(planList == null || planList.isEmpty())
				return ResponseDto.noExistPlanList();
			
			for(PlanRequestDto plan : planList)
				plan.setUserId(userId);
			
			int result = planDao.insertPlanBatch(planList);
			
			if(result == 0) return ResponseDto.databaseError();
			
			int userResult = userDao.updateUserQuotes(dto.getQuotes(), userId);
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
	public ResponseEntity<ResponseDto> postPlanCompl(int planPk, String userId) {

		try {
			
			LocalDate localDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = localDate.format(formatter);
			
			int result = planDao.postPlanCompl(planPk, formattedDate);
			
			if(result == 0) return ResponseDto.noExistPlan();
			
			eventPublisher.publishEvent(new PlanCompleteEvent(planPk, userId));
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}
	
	@Override
	@Transactional
	public ResponseEntity<ResponseDto> postPlanUncompl(int planPk, String userId) {

		try {
			
			int result = planDao.postPlanUncompl(planPk);
			
			if(result == 0) return ResponseDto.noExistPlan();
			
			eventPublisher.publishEvent(new PlanUncompleteEvent(planPk, userId));
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseDto> postponeWeeklyPlan(String userId) {
		
		try {
			
			int result = planDao.postponeWeeklyPlan(userId);
			
			if(result == 0) return ResponseDto.databaseError();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.CREATED);
		
	}
	
	@Override
	public ResponseEntity<ResponseDto> insertWeeklyPlan(PlanRegistRequestDto dto, String userId) {

		try {
			
			List<PlanRequestDto> planList = dto.getPlans();
			
			if(planList == null || planList.isEmpty())
				return ResponseDto.noExistPlanList();
			
			for(PlanRequestDto plan : planList)
				plan.setUserId(userId);
			
			int result = planDao.insertPlanBatch(planList);
			if(result == 0) return ResponseDto.databaseError();
			
			int userResult = userDao.updateUserQuotes(dto.getQuotes(), userId);
			if(userResult == 0) return ResponseDto.databaseError();
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.CREATED);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<ResponseDto> shiftTodayPlanToTomorrow(String userId) {

		try {
			
			int result = planDao.shiftTodayPlanToTomorrow(userId);
			
			if(result == 0) return ResponseDto.databaseError();
		}
		catch (Exception e) {
			e.printStackTrace();
	        return ResponseDto.databaseError();
		}
		
		return ResponseDto.success(HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseDto> updateTodayPlan(PlanRequestDto dto, String userId) {
		
		try {
			
			// userId를 이용해 계획을 조회
			PlanRequestDto plan = planDao.selectPlan(dto.getPlanPk(), userId);
		
			if(plan == null)
				return ResponseDto.noExistPlan();
			
			dto.setPlanPk(plan.getPlanPk());
			
			int result = planDao.updateTodayPlan(dto);
			if(result == 0) return ResponseDto.databaseError();
			
			int userResult = userDao.updateUserQuotes(dto.getQuotes(), userId);
			if(userResult == 0) return ResponseDto.databaseError();
			
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
	private final PetDao petDao;
	
	@Async
	@TransactionalEventListener
	public void handlePlanCompl(PlanCompleteEvent event) {
		reviewService.insertReview(event.getPlanPk());
		petDao.increaseScore(event.getUserId());
	}
	
	@Async
	@TransactionalEventListener
	public void handlePlanUncompl(PlanUncompleteEvent event) {
//		reviewService.updateReview(new PutReviewRequestDto(event.getPlanPk(), null, null, null));
		petDao.decreaseScore(event.getUserId());
	}
}
