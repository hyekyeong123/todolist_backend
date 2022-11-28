package com.codingcat.todolist.security;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = false)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  @Autowired JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors()
      .and()
      .csrf().disable()
      .httpBasic().disable() // token 사용
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안함
      .and()
      .authorizeRequests()
      .antMatchers("/","/auth/**").permitAll() // 이 경로는 인증 안함
      .anyRequest().authenticated(); // 나머지는 모두 인증 필요

      // Cors 필터 등록한 후에 JwtAuthentication 필터를 실행한다.
      http.addFilterAfter(
        jwtAuthenticationFilter,
        CorsFilter.class
      );

    return http.build();
  }
}
