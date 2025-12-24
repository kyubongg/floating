package com.floating.mvc.dto.response.plan;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.plan.PlanRegistRequestDto;
import com.floating.mvc.dto.request.plan.PlanRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class GetPlanListResponseDto extends ResponseDto  {

	private List<PlanRequestDto> planList;
		
	private GetPlanListResponseDto(List<PlanRequestDto> dto) {
		this.planList = dto;
	}
	
	public static ResponseEntity<GetPlanListResponseDto> success(List<PlanRequestDto> dto){
		
		GetPlanListResponseDto body = new GetPlanListResponseDto(dto);
		
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
