package com.todo.api.controller;

import com.todo.api.entity.Todo;
import com.todo.api.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<Todo>> findAll(
        @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(todoService.findAll(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Todo> create(
        @AuthenticationPrincipal String email,
        @Valid @RequestBody Todo todo
    ) {
        return ResponseEntity.status(201).body(todoService.create(email, todo));
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
