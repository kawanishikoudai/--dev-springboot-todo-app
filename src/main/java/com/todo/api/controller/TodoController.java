package com.todo.api.controller;

import com.todo.api.entity.Todo;
import com.todo.api.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<Todo> findAll() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo) {
        return ResponseEntity.status(201).body(todoService.create(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(
        @PathVariable Long id,
        @RequestBody Todo todo
    ) {
        return ResponseEntity.ok(todoService.update(id, todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
