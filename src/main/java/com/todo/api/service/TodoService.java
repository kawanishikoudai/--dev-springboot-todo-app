package com.todo.api.service;

import com.todo.api.entity.Todo;
import com.todo.api.entity.User;
import com.todo.api.repository.TodoRepository;
import com.todo.api.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public List<Todo> findAll(String email) {
        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new RuntimeException("ユーザーが見つかりません。")
            );
        return todoRepository.findByUser(user);
    }

    public Todo findById(Long id) {
        return todoRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Todoが見つかりません。"));
    }

    public Todo create(String email, Todo todo) {
        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new RuntimeException("ユーザーが見つかりません。")
            );
        todo.setUser(user);
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
