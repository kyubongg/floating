package com.floating.mvc.dto.request.wbti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WbtiRequestDto {
	private String userId;
	private int socialType;
	private int motivationType;
	private int executionType;
	private int activityType;
}
