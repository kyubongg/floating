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
	
	private GetPlanDetailResponseDto(PlanRequestDto dto) {
		this.date = dto.getCompleteDate();
		this.category = dto.getCategory();
		this.detail = dto.getDetail();
		this.time = dto.getTime();
		this.completeDate = dto.getCompleteDate();
		this.shifted = dto.getShifted();
	}
	
	public static ResponseEntity<GetPlanDetailResponseDto> success(PlanRequestDto dto){
		
		GetPlanDetailResponseDto body = new GetPlanDetailResponseDto(dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
