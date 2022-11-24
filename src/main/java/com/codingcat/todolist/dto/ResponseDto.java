package com.codingcat.todolist.dto;

import com.codingcat.todolist.model.TodoEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
  private String error;
  private List<T> data;
}