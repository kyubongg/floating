package com.floating.mvc.dto.response.user;

import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.floating.mvc.dto.request.user.SignInRequestDto;
import com.floating.mvc.dto.request.user.UserRequestDto;
import com.floating.mvc.dto.response.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDto extends ResponseDto {
	private String name;
	private Date birth;
	private String mbtiName;
	private String accessToken;
	private Integer expiration;
	
	private SignInResponseDto(UserRequestDto dto, String accessToken) {
		this.name = dto.getName();
		this.birth = dto.getBirth();
		this.mbtiName = dto.getMbtiName();
		this.accessToken = accessToken;
		this.expiration = 60 * 60 * 9;		// JwtProvider에 9시간으로 설정해놔서 9시간으로 넘겨주는거야. 
											// 9시간 지나면 accessToken이 만료된다는 뜻이고, 시간은 그냥 9시간으로 했어. 바꿔도 돼!
	}
	
	public static ResponseEntity<SignInResponseDto> success(UserRequestDto dto, String accessToken){
		SignInResponseDto body = new SignInResponseDto(dto, accessToken);
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
}
