package com.codingcat.todolist.controller;

import com.codingcat.todolist.dto.ResponseDto;
import com.codingcat.todolist.dto.TodoDto;
import com.codingcat.todolist.model.TodoEntity;
import com.codingcat.todolist.service.TodoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo")
public class TodoController {

  @Autowired
  TodoService todoService;

  @GetMapping
  public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){

    // 사용자의 todoList 목록 가져오기
    List<TodoEntity> entities = todoService.retrieve(userId);
    List<TodoDto> todoDtos = entities.stream()
      .map(TodoDto::new)
      .collect(Collectors.toList());
    ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
      .data(todoDtos)
      .build();

    return ResponseEntity.ok().body(response);
  }

  @PostMapping
  public ResponseEntity<?> createTodo(
    @AuthenticationPrincipal String userId,
    @RequestBody TodoDto dto
  ){
    try{

      // TodoEntity로 변환
      TodoEntity todoEntity = TodoDto.toEntity(dto);
      todoEntity.setId(null); // 이게 굳이 필요한가?
      todoEntity.setUserId(userId);

      // 저장
      List<TodoEntity> entities = todoService.create(todoEntity);

      // 자바 스트림을 이용해 리턴된 엔티티 리스트를 todoDto 리스트로 변환
      List<TodoDto> todoDtos = entities.stream()
        .map(entity -> new TodoDto(entity))
        .collect(Collectors.toList());

      // 변환된 TodoDto리스트를 이용해 ResponseDto를 초기화한다.
      ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
        .data(todoDtos)
        .build();

      return ResponseEntity.ok().body(response);
    }catch (Exception e){
      String error = e.getMessage();
      ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
        .error(error)
        .build();
      return ResponseEntity.badRequest().body(response);
    }
  }

  @DeleteMapping
  public ResponseEntity<?> deleteTodo(
    @AuthenticationPrincipal String userId,
    @RequestBody TodoDto dto
  ){
    try{
      // TodoEntity로 변환
      TodoEntity todoEntity = TodoDto.toEntity(dto);
      todoEntity.setUserId(userId);

      // 저장
      List<TodoEntity> entities = todoService.delete(todoEntity);

      // 자바 스트림을 이용해 리턴된 엔티티 리스트를 todoDto 리스트로 변환
      List<TodoDto> todoDtos = entities.stream()
        .map(TodoDto::new)
        .collect(Collectors.toList());

      // 변환된 TodoDto리스트를 이용해 ResponseDto를 초기화한다.
      ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
        .data(todoDtos)
        .build();

      return ResponseEntity.ok().body(response);
    }catch (Exception e){
      String error = e.getMessage();
      ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
        .error(error)
        .build();
      return ResponseEntity.badRequest().body(response);
    }
  }
}
