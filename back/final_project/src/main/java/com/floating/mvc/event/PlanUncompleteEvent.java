package com.floating.mvc.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanUncompleteEvent {
	private final int planPk;
	private final String userId;
	private final String content = null;
	
	public PlanUncompleteEvent(int planPk, String userId) {
		this.planPk = planPk;
		this.userId = userId;
	}
}
