package com.floating.mvc.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// class: Jwt 생성 및 검증 기능 제공자 //
// description: JWT 암호화 알고리즘 HS256 //
// description: 암호화에 사용되는 Secret Key는 환경변수에 jwt.secret으로 사용 //
// description: JWT 만료 기간 9시간 //

@Component
public class JwtProvider {
    
    @Value("${jwt.secret}")
    private String secretKey;

    // function: JWT 생성 메서드 //
    // description: 생성되는 JWT의 payload에는 sub, iat, exp 값이 들어가도록 //
    // description: sub - 사용자의 아이디, iat - 생성시 시간, exp - 생성시간 + 9시간 //
    public String create(String userId){
        
        Date expiration = Date.from(Instant.now().plus(9, ChronoUnit.HOURS));
        
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        String jwt = null;

        try{
        	/**
        	 * JWT 구조 : header - payload - signature  
        	 * 
        	 * signWith: JWT의 서명을 정의  
        	 * subject: payload에 sub(제목) 클레임을 설정함 (토큰의 주체를 식별하는 고유한 값: userId 사용)
        	 * issuedAt: payload에 iat(발생 시간) 클레임을 설정함
        	 * expiration: payload에 exp(만료 시간) 클레임을 설정함
        	 * compact: 지금까지 설정된 header, payload, signature 정보를 모두 조합하여
        	 * 			최종적인 JWT를 생성하고 반환함
        	 * 
        	 */
        	
            jwt = Jwts.builder()
            		.signWith(key)
            		.subject(userId)
            		.issuedAt(new Date())
            		.expiration(expiration)
            		.compact();

        }catch(Exception exception){
            exception.printStackTrace();
        }

        return jwt;
    }

    // function: JWT 검증 메서드 //
    // description: 검증이 성공적으로 완료되면 subject에 있는 userId 반환 //
    public String validate(String jwt){

        String userId = null;

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try{
            userId = Jwts.parser()
            			.verifyWith(key)
            			.build()
            			.parseSignedClaims(jwt)
            			.getPayload()
            			.getSubject();
            		
        } catch (ExpiredJwtException e) {
//            log.error("JWT expired at {}. Current time: {}. Error: {}",
//                        e.getClaims().getExpiration(),
//                        System.currentTimeMillis(),
//                        e.getMessage());
            e.printStackTrace();
            return null;  
        } catch (JwtException e) {
//            log.error("JWT validation failed. Error: {}", e.getMessage());
        	e.printStackTrace();
            return null;
        } catch (Exception e) {
//            log.error("Unexpected error: {}", e.getMessage());
        	e.printStackTrace();
            return null;
        }

        return userId;
    }
}