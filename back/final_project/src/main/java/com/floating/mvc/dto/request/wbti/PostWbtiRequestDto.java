package com.floating.mvc.dto.request.wbti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostWbtiRequestDto {
	
	private int socialType;
	private int motivationType;
	private int executionType;
	private int activityType;
}
