package com.codingcat.todolist.service;

import com.codingcat.todolist.model.TodoEntity;
import com.codingcat.todolist.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TodoService {

  @Autowired private TodoRepository todoRepository;

  // 사용자가 기록했던 todoList 가져오기
  public List<TodoEntity> retrieve(final String userId){
    return todoRepository.findByUserId(userId);
  }

  public List<TodoEntity> create(final TodoEntity entity){
    validate(entity);

    todoRepository.save(entity);
    log.info("Entity Id : {} is saved", entity.getId());

    return todoRepository.findByUserId(entity.getUserId());
  }


  private void validate(final TodoEntity entity){
    if(entity == null){
      log.warn("Entity cannot be null");
      throw new RuntimeException("Entity cannot be null");
    }

    if(entity.getUserId() == null){
      log.warn("unknown user");
      throw new RuntimeException("unknown user.");
    }
  }

  public List<TodoEntity> update(final TodoEntity entity){
    validate(entity);

    // 넘겨 받은 entity id를 이용해 DB에 저장된 TodoEntity를 가져온다.
    final Optional<TodoEntity> old = todoRepository.findById(entity.getId());

    // 반환된 entity가 존재한다면 값을 새 entity의 값으로 덮어 씌운다.
    old.ifPresent(todo -> {
      todo.setTitle(entity.getTitle());
      todo.setDone(entity.isDone());

      // 데이터베이스에 새 값을 저장
      todoRepository.save(todo);
    });
    return retrieve(entity.getUserId());
  }

  public List<TodoEntity> delete(final TodoEntity entity){
    validate(entity);
    try {
      todoRepository.delete(entity);
    }catch (Exception e){
      log.error("error deleting entity ", entity.getId(), e);

      // 컨트룰러로 exception 전달
      // 데이터베이스 내부 로직을 캡슐화 하기위해 e를 리런하지 않고 새 exception 오브젝트를 리턴
      throw new RuntimeException("error deleting entity "+ entity.getId());
    }
    return retrieve(entity.getUserId());
  }
}
