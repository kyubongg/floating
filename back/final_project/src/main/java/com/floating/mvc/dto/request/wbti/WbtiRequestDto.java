package com.floating.mvc.dto.request.wbti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WbtiRequestDto {
	private String userId;
	private String wbtiCode;
	private int sociaScore;
	private int motivationScore;
	private int executionScore;
	private int activityScore;
	private String aiAnalysis;
	private String aiEconomicTip;
	private String pastExperiences;
	private String bodyConditions;
	private String availableTime;
}
