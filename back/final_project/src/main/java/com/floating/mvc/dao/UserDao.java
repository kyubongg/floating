package com.floating.mvc.dao;

import java.util.List;

import com.floating.mvc.dto.request.user.FindInfoRequestDto;
import com.floating.mvc.dto.request.user.PutUserMbtiRequestDto;
import com.floating.mvc.dto.request.user.PutUserRequestDto;
import com.floating.mvc.dto.request.user.SignupRequestDto;
import com.floating.mvc.dto.request.user.UserRequestDto;

public interface UserDao {
	
	//회원가입 
	int insertUser(SignupRequestDto dto);
	
	//중복 체크
    boolean existById(String id);
    boolean existByEmail(String email);
    
    //회원 상세 조회
    UserRequestDto selectUser(String userId);
    
    //회원 정보 수정 
    int updateUser(PutUserRequestDto dto, String userId); 
    
    //회원 Mbti 정보 수정
    int updateUserMbti(PutUserMbtiRequestDto dto);
    
    //회원 탈퇴
    int deleteUser(String userId);
    
    //비밀번호 확인
    String selectPasswordById(String userId);
    
    //사용자 조회 (이름, 이메일 입력)
    boolean existUser(FindInfoRequestDto dto);
    
    //아이디 조회
    String selectIdByEmail(String email);
    
    //비밀번호 재설정
    int updatePassword(FindInfoRequestDto dto);
    
    // 회원 응원 문구 수정
    int updateUserQuotes(List<String> quotes, String userId);
}
