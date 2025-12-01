package com.floating.mvc.dto.request.plan;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanRequestDto {
	
	private int userPk;
	private java.util.Date date;
	private String category;
	private String detail;
	private int time;
	private java.util.Date completeDate;
	private int shifted;
	
	public PlanRequestDto() {
		
	}

	public PlanRequestDto(int user_pk, Date date, String category, String detail, int time,
			Date complete_date, int shifted) {
		super();
		this.userPk = userPk;
		this.date = date;
		this.category = category;
		this.detail = detail;
		this.time = time;
		this.completeDate = completeDate;
		this.shifted = shifted;
	}
	
}
