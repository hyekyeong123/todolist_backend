package com.codingcat.todolist.controller;

import com.codingcat.todolist.dto.ResponseDto;
import com.codingcat.todolist.service.TodoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @Autowired
  private TodoService todoService;

  @GetMapping
  public String testController(){
    return "Hello World!";
  }

  @GetMapping("/{id}")
  public String testController(
    @PathVariable(required = false) int id
  ){
    return "Hello World! ID : "+id;
  }

  // ResponseDto 사용
  @GetMapping("/testResponseBody")
  public ResponseDto<String> testControlllerResponseBody(){
    List<String> list = new ArrayList<>();
    list.add("Hello World!");
    ResponseDto<String> responseDto = ResponseDto.<String>builder().data(list).build();
    return responseDto;
  }

  // ResponseEntity 사용
  @GetMapping("/testResponseEntity")
  public ResponseEntity<?> testControllerResponseEntity(){
    List<String> list = new ArrayList<>();
    list.add("Hello World! And you got 400");
    ResponseDto<String> responseDto = ResponseDto.<String>builder().data(list).build();
    return ResponseEntity.badRequest().body(responseDto);
  }
}
