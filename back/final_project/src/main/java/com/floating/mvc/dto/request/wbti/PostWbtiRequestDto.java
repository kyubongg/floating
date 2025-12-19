package com.floating.mvc.dto.request.wbti;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostWbtiRequestDto {
    private String userId;
    private String wbtiCode;
    
    private double socialScore;
    private double motivationScore;
    private double executionScore;
    private double activityScore;

    private String aiAnalysis;
    private String aiEconomicTip;

    // 1. 프론트에서 JSON으로 받는 리스트
    private List<String> experiences;
    private List<String> bodyConditions;

    // 2. DB(MyBatis)에 저장하기 위한 변환용 필드
    private String experiencesString;
    private String bodyConditionsString;

    private String quitReason;
    private String availableTime;
    private String economy;
    
    // 프론트에서 받은 데이터를 문자열로 변환
    public void setExperiences(List<String> experiences) {
        this.experiences = experiences;
        if (experiences != null && !experiences.isEmpty()) {
            this.experiencesString = String.join(", ", experiences);
        }
    }

    public void setBodyConditions(List<String> bodyConditions) {
        this.bodyConditions = bodyConditions;
        if (bodyConditions != null && !bodyConditions.isEmpty()) {
            this.bodyConditionsString = String.join(", ", bodyConditions);
        }
    }
}
