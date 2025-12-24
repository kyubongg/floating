package com.floating.mvc.dto.response.wbti;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.wbti.PostWbtiRequestDto;
import com.floating.mvc.dto.request.wbti.WbtiRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class GetWbtiResponseDto extends ResponseDto {
    
    private String wbtiCode;
    private double socialScore;
    private double motivationScore;
    private double executionScore;
    private double activityScore;

    // DB의 JSON 데이터를 Map으로 받아 프론트에 그대로 전달
    private Map<String, Object> aiResult;
    private Map<String, Object> userConditions;

    // 생성자: 조회된 데이터를 필드에 매핑
    private GetWbtiResponseDto(WbtiRequestDto dto) {
        super(); // ResponseDto의 초기화가 필요하다면 호출
        this.wbtiCode = dto.getWbtiCode();
        this.socialScore = dto.getSocialScore();
        this.motivationScore = dto.getMotivationScore();
        this.executionScore = dto.getExecutionScore();
        this.activityScore = dto.getActivityScore();
        this.aiResult = dto.getAiResult();
        this.userConditions = dto.getUserConditions();
    }

    public static ResponseEntity<GetWbtiResponseDto> success(WbtiRequestDto dto){
        GetWbtiResponseDto body = new GetWbtiResponseDto(dto);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}