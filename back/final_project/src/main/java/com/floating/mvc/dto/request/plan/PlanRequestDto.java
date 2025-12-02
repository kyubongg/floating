package com.floating.mvc.dto.request.plan;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequestDto {
	
	private Date date;
	private String category;
	private String detail;
	private int time;
	private Date completeDate;
	private int shifted;
	
	
}
