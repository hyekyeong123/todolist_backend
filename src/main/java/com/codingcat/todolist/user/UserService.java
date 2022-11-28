package com.codingcat.todolist.user;

import com.codingcat.todolist.user.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  // 유저 생성하기
  public UserEntity create(final UserEntity userEntity){
    if(userEntity == null || userEntity.getUsername() == null)
      throw new RuntimeException("Invalid arguments");

    final String username = userEntity.getUsername();
    if(userRepository.existsByUsername(username)){
      log.warn("이 유저네임은 이미 존재합니다."+ username);
      throw new RuntimeException("Username already exists");
    }
    return userRepository.save(userEntity);
  }

  // 이메일과 비밀번호가 일치하는지 확인
  public UserEntity getByCredentials(final String username, final String password){
    return userRepository.findByUsernameAndPassword(username, password);
  }
}
