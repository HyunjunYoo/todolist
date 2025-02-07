package com.cspi.backend.mapper;

import com.cspi.backend.model.Todo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TodoMapper {
    void insertTodo(Todo todo);
    List<Todo> getAllTodos();
    List<Todo> searchTodos(Todo searchCriteria);
    void updateTodoById(Todo todo);
    void deleteTodoById(Long id);
    Todo findById(Long id);
}

