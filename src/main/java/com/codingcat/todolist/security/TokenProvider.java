package com.codingcat.todolist.security;

import com.codingcat.todolist.user.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {
  private static final String SECRET_KEY="rpWQzmcu62OUYmDihxhC1PrTnQnSE4uUTEAGf1k02g4b67LMIerpWQzmcu62OUYmDihxhC1PrTnQnSE4uUTEAGf1k02g4b67LMIe";

  // JWT Token 생성
  public String tokenCreate(UserEntity userEntity){

    return Jwts.builder()
      .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
      .setSubject(userEntity.getId())
      .setIssuer("MEDISTAFF")
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis()  + +  1 * (1000 * 60 * 60 * 24) ) )
      .compact();
  }

  // 토큰 유효성 검사  후 userId 리턴
  public String validateAndGetUserId(String token){
    Claims claims = Jwts.parser()
      .setSigningKey(SECRET_KEY)
      .parseClaimsJws(token)
      .getBody();

    return claims.getSubject();
  }
}
