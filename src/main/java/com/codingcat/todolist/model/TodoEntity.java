package com.codingcat.todolist.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="todo")
public class TodoEntity {

  @Id
  @GeneratedValue(generator = "system-uuid") // 사용
  @GenericGenerator(name="system-uuid",strategy = "uuid") // uuid를 사용하는 system-uuid라는 이름의 GenericGenerator 생성
  private String id; // 고유번호

  private String userId; // 유저의 아이디
  private String title; // 제목
  private boolean done; // todo를 완료하였는지
}
