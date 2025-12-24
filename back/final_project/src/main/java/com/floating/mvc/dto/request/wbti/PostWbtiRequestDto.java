package com.floating.mvc.dto.request.wbti;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostWbtiRequestDto {
    private String userId;
    private String wbtiCode;
    
    private double socialScore;
    private double motivationScore;
    private double executionScore;
    private double activityScore;

    // JSON 컬럼에 매핑될 객체들
    private Map<String, Object> aiResult;
    private Map<String, Object> userConditions;
}