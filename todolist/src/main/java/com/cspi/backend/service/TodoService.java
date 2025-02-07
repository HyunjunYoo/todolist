package com.cspi.backend.service;

import com.cspi.backend.exception.TodoNotFoundException;
import com.cspi.backend.mapper.TodoMapper;
import com.cspi.backend.model.Todo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

//@Transactional // 데이터베이스의 일관성과 무결성을 유지
@Service
public class TodoService {
    private final TodoMapper todoMapper;

    public TodoService(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = todoMapper.getAllTodos();
        if(todos.isEmpty()) {
            throw new TodoNotFoundException("등록된 할 일이 없습니다.");
        }
        return todos;
    }

    public List<Todo> searchTodos(Todo searchCriteria) {
        List<Todo> todos = todoMapper.searchTodos((searchCriteria));
        if(todos.isEmpty()) {
            throw new TodoNotFoundException("등록된 할 일이 없습니다.");
        }
        return todoMapper.searchTodos(searchCriteria);
    }

    public void addTodo(Todo todo) {
        if(todo.getTodoText() == null || todo.getTodoText().trim().isEmpty()) {
            throw new IllegalArgumentException("할 일 내용은 비워둘 수 없습니다.");
        }
        todoMapper.insertTodo(todo);
    }

    public void updateTodoById(Todo todo) {
        Long id = todo.getId(); //가 Null 인 경우
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("ID 값이 유효하지 않습니다." + id);
        }

        Todo existingTodo = todoMapper.findById(id);
        if(existingTodo == null) {
            throw new NoSuchElementException("해당 ID의 할 일이 존재하지 않습니다." + id);
        }

        String todoText = todo.getTodoText();
        if(todoText == null || todoText.trim().isEmpty()) {
            throw new IllegalArgumentException("할 일 내용은 비워둘 수 없습니다.");
        }

        Todo updateTodo = Todo.builder()
                .id(existingTodo.getId())
                .status(existingTodo.getStatus())
                .todoText(todoText)
                .startDate(existingTodo.getStartDate())
                .endDate(existingTodo.getEndDate())
                .build();
        todoMapper.updateTodoById(updateTodo);
    }

    public void updateTodoStatus(Todo todo) {
        Long id = todo.getId();
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("ID 값이 유효하지 않습니다." + id);
        }
        Todo existingTodo = todoMapper.findById(id);
        if(existingTodo == null) {
            throw new IllegalArgumentException("해당 ID의 할 일이 존재하지 않습니다." + id);
        }
        String status = todo.getStatus();
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("상태 값은 비워둘 수 없습니다.");
        }

        List<String> validStatuses = Arrays.asList("시작 전","진행 중","완료");
        if(!validStatuses.contains(status)) {
            throw new IllegalArgumentException("유효하지 않은 상태 값입니다." + status);
        }

        Todo updateTodo = Todo.builder()
                .id(existingTodo.getId())
                .status(status)
                .todoText(existingTodo.getTodoText())
                .startDate((existingTodo.getStartDate()))
                .endDate(existingTodo.getEndDate())
                .build();
        todoMapper.updateTodoById(updateTodo);
    }

    public void deleteTodoById(Long id) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("ID 값이 유효하지 않습니다." + id);
        }

        Todo existingTodo = todoMapper.findById(id);
        if(existingTodo == null) {
            throw new NoSuchElementException("해당 ID의 할 일이 존재하지 않습니다." + id);
        }

        todoMapper.deleteTodoById(id);
    }
}
