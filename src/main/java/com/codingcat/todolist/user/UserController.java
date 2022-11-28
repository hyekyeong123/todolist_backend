package com.codingcat.todolist.user;

import com.codingcat.todolist.dto.ResponseDto;
import com.codingcat.todolist.security.TokenProvider;
import com.codingcat.todolist.user.model.UserDto;
import com.codingcat.todolist.user.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
  @Autowired private UserService userService;
  @Autowired private TokenProvider tokenProvider;


  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
    try {

      // 리퀘스트를 이용해 저장할 유저 만들기
      UserEntity user = UserEntity.builder()
        .username(userDto.getUsername())
//        .password(passwordEncoder.encode(userDto.getPassword()))
        .password(userDto.getPassword())
        .build();

      // 서비스를 이용해 리파지토리에 유저 저장
      UserEntity registeredUser = userService.create(user);

      UserDto responseUserDTO = UserDto.builder()
        .id(registeredUser.getId())
        .username(registeredUser.getUsername())
        .build();

      // 유저 정보는 항상 하나이므로 그냥 리스트로 만들어야하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴.
      return ResponseEntity.ok(responseUserDTO);
    } catch (Exception e) {
      // 예외가 나는 경우 bad 리스폰스 리턴.
      ResponseDto<Object> responseDto = ResponseDto.builder().error(e.getMessage()).build();
      return ResponseEntity
        .badRequest()
        .body(responseDto);
    }
  }

  // 로그인
  @PostMapping("/signin")
  public ResponseEntity<?> authenticate(@RequestBody UserDto userDto) {

    UserEntity user = userService.getByCredentials(
      userDto.getUsername(),
      userDto.getPassword()
//      passwordEncoder
    );

    if(user != null) {

      // 토큰 생성
      final String token = tokenProvider.tokenCreate(user);
      log.error("token : "+token);

      final UserDto responseUserDTO = UserDto.builder()
        .username(user.getUsername())
        .id(user.getId())
        .token(token)
        .build();
      return ResponseEntity.ok().body(responseUserDTO);
    } else {
      ResponseDto<Object> responseDTO = ResponseDto.builder()
        .error("Login failed.")
        .build();
      return ResponseEntity
        .badRequest()
        .body(responseDTO);
    }
  }
}
