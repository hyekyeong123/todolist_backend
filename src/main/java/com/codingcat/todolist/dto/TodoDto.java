package com.codingcat.todolist.dto;

import com.codingcat.todolist.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {
  private String id; // 고유번호
  private String title; // 제목
  private boolean done; // todo를 완료하였는지

  // TodoEntity -> TodoDto로 변환
  public TodoDto(final TodoEntity entity){
    this.id = entity.getId();
    this.title = entity.getTitle();
    this.done = entity.isDone();
  }

  // TodoDto -> TodoEntity로 변환
  public static TodoEntity toEntity(final TodoDto dto){
    return TodoEntity.builder()
      .id(dto.getId())
      .title(dto.getTitle())
      .done(dto.isDone())
      .build();
  }
}