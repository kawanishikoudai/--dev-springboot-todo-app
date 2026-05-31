package com.todo.api.service;

import com.todo.api.entity.Todo;
import com.todo.api.repository.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo findById(Long id) {
        return todoRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Todoが見つかりません。"));
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Long id, Todo todo) {
        Todo existing = findById(id);
        existing.setTitle(todo.getTitle());
        existing.setCategory(todo.getCategory());
        existing.setStatus(todo.getStatus());
        existing.setDueDate(todo.getDueDate());
        return todoRepository.save(existing);
    }

    public void delete(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todoが見つかりません。");
        }
        todoRepository.deleteById(id);
    }
}
