package com.floating.mvc.dto.response.plan;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPlanDetailResponseDto extends ResponseDto {
	
	private Date date;
	private String category;
	private String detail;
	private int time;
	private Date completeDate;
	private int shifted;
	
	private GetPlanDetailResponseDto(PlanRequestDto plan) {
		
		this.date = plan.getCompleteDate();
		this.category = plan.getCategory();
		this.detail = plan.getDetail();
		this.time = plan.getTime();
		this.completeDate = plan.getCompleteDate();
		this.shifted = plan.getShifted();
	}
	
	public static ResponseEntity<GetPlanDetailResponseDto> success(PlanRequestDto dto){
		
		GetPlanDetailResponseDto body = new GetPlanDetailResponseDto(dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
