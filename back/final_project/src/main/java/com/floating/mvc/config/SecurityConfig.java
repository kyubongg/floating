package com.floating.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.floating.mvc.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  
    //jwt
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
    	security
        	// description: CSRF 취약점 대비 미사용 지정 //
            .csrf(csrf -> csrf.disable())
            // description: CORS 정책 설정 //
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // description: session 유지하지 않음 지정 //
            .sessionManagement(session -> session
            		.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // description: form 형태 로그인 막음 지정 //
            .formLogin(form -> form.disable())
            // description: Basic 인증 미사용 지정 //
            .httpBasic(basic -> basic.disable())
            // description: 인가 설정 //
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/user/**").permitAll()
                .requestMatchers("/api/v1/wbti/**").permitAll()
                .requestMatchers("/images/review/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .anyRequest().authenticated()  // 나머지는 인증 필요
            )
            // description: Jwt Authentication Filter 등록 //
            .addFilterBefore(
            		jwtAuthenticationFilter, 
            		UsernamePasswordAuthenticationFilter.class
            );
        
        return security.build();
    }
    
    // CORS 정책 설정 객체를 반환하는 메서드 //
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
    	
    	/**
    	 * 
    	 * CorsConfigurationSource
    	 * http://localhost:5173에서 오는 모든 API 요청에 대해 인증 정보(JWT, 쿠키)를
    	 * 포함한 통신을 허용하도록 통신을 허용하는 서버를 설정하는 역할
    	 * 
    	 * addAllowedHeader: 허용할 요청 헤더 설정
    	 * addAllowedMethod: 허용할 HTTP 메서드 설정
    	 * addAllowedOrigin: 허용할 출처 설정(프론트엔드 개발 서버 주소만 명시적으로 허용)
    	 * setAllowCredentials: 인증 정보(JWT, 쿠키 등) 허용 설정
    	 */
    	CorsConfiguration configuration = new CorsConfiguration();
    	
    	configuration.addAllowedHeader("*");
    	configuration.addAllowedMethod("*");
    	configuration.addAllowedOrigin("http://localhost:5173");
    	configuration.setAllowCredentials(true);
    	
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	return source;
    }
}
