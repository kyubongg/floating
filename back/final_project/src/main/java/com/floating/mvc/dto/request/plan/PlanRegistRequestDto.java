package com.floating.mvc.dto.request.plan;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanRegistRequestDto {
	
	private List<PlanRequestDto> plans;
	
}
