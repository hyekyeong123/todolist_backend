package com.codingcat.todolist.repository;

import com.codingcat.todolist.model.TodoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

  List<TodoEntity> findByUserId(String userId);
}
