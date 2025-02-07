package com.cspi.backend.controller;

import com.cspi.backend.model.Todo;
import com.cspi.backend.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 객체를 JSON 형태로 반환
@RequiredArgsConstructor // 객체를 직접 생성하지 않고 외부(스프링 컨테이너)에 위임
@CrossOrigin(origins = "http://localhost:5173") // 출발하는 위치을 뜻함(CORS)
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping("/search")
    public List<Todo> searchTodos(@RequestBody Todo searchCriteria) { //@RequestBody JSON 데이터를 Java 객체로 변환
        List<Todo> todos = todoService.searchTodos(searchCriteria);
        return todos;
    }

    @PostMapping("/add")
    public void addTodo(@RequestBody List<Todo> todos) {
        for (Todo todo : todos) {
            todoService.addTodo(todo);
        }
    }

    @PutMapping("/update")
    public void updateTodoText(@RequestBody Todo todo) {
        todoService.updateTodoById(todo);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateTodoStatus(@RequestBody Todo todo) {
        todoService.updateTodoStatus(todo);
        return ResponseEntity.ok("상태 저장 성공");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteTodo(@RequestBody Todo todo) {
        todoService.deleteTodoById(todo.getId());
        return ResponseEntity.ok("삭제 완료");
        }
}
