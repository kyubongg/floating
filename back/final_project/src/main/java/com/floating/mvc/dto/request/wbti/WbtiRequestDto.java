package com.floating.mvc.dto.request.wbti;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WbtiRequestDto {
    private String userId;
    private String wbtiCode;
    
    private Float socialScore;
    private Float motivationScore;
    private Float executionScore;
    private Float activityScore;
    
    // JSON 컬럼에 매핑될 객체들
    private Map<String, Object> aiResult;
    private Map<String, Object> userConditions;
}