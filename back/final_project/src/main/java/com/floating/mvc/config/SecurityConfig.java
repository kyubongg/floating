package com.floating.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .formLogin(form -> form.disable())  // 기본 로그인 폼 비활성화
            .httpBasic(basic -> basic.disable());  // Basic 인증 비활성화
        
        return http.build();
    }
    
    
      //jwt
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/v1/auth/**").permitAll()  // 로그인/회원가입 허용
//                .requestMatchers("/api/v1/public/**").permitAll()  // 공개 API 허용
//                .anyRequest().authenticated()  // 나머지는 인증 필요
//            );
//        
//        return http.build();
//    }
}
