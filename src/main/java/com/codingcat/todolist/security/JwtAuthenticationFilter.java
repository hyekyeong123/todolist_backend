package com.codingcat.todolist.security;

import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {
    try{
      // 요청에서 토큰 가져오기
      String token  = parseBearerToken(request);
      log.info("Filter is running...");

      // 토큰 검사하기 JWT이므로 인가 서버에 요청하지 않고도 검증 가능
      if(token != null && !token.equalsIgnoreCase("null")){
        String userId = tokenProvider.validateAndGetUserId(token);
        log.info("Authentication User ID : "+userId);

        // 인증 완료(SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다)
        AbstractAuthenticationToken authenticationToken
          = new UsernamePasswordAuthenticationToken(
          userId,null, AuthorityUtils.NO_AUTHORITIES
        );

        // TODO : 이 부분은 내가 짠거랑 다른데 => return getAuthenticationManager().authenticate(token);
        // SecurityContext에 인증된 사용자를 등록
        authenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request));;

        // SecurityContextHolder에 SecurityContext를 등록
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);

        SecurityContextHolder.setContext(securityContext);
      }
    }catch (Exception e){
      logger.error("Could not set User authentication is security context",e);
    }
    filterChain.doFilter(request, response);
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
