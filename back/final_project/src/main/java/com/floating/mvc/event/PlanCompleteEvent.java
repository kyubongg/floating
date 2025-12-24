package com.floating.mvc.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class PlanCompleteEvent {
	private final int planPk;
	private final String userId;
	private final String content = null;
	
	public PlanCompleteEvent(int planPk, String userId) {
		this.planPk = planPk;
		this.userId = userId;
	}
}
