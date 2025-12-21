package com.floating.mvc.dto.request.plan;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlanRequestDto {
	
	private int planPk;
	private Date date;
	private String category;
	private String detail;
	private int time;
	private String userId;
	private Date completeDate;
	private int shifted = 0;
}