package com.codingcat.todolist.test;

import static org.springframework.util.StringUtils.hasText;

import antlr.StringUtils;
import com.codingcat.todolist.security.TokenProvider;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExampleServletFilter extends HttpFilter {
  private TokenProvider tokenProvider;

  @Override
  public void doFilter(
    HttpServletRequest request, HttpServletResponse response, FilterChain chain
  ) throws IOException, ServletException {

    try{
      final String token = parseBearerToken(request);
      if(token != null && !token.equalsIgnoreCase("null")){

        // userId 가져오기 위조된 경우 예외 처리함
        String userId = tokenProvider.validateAndGetUserId(token);
        chain.doFilter(request, response);  // 다음 ServletFilter 실행
      }

      super.doFilter(request, response, chain);
    }catch (Exception e){
      // 예외 발생시 response를 403 Forbidden으로 설정
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
  }

  // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴
  private String parseBearerToken(HttpServletRequest request) {

    // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴
    String bearerToken = request.getHeader("Authorization");

    if(hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
      return bearerToken.substring(7);
    }
    return null;
  }


}
