package com.todo.api.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.todo.api.entity.Todo;
import com.todo.api.repository.TodoRepository;
import com.todo.api.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TodoIntegrationTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @Test
    void TODOリストを作成して取得できること() {
        String expandValue = "統合テスト：TODOリストを作成して取得できること";
        Todo todo = new Todo();
        todo.setTitle(expandValue);
        Todo saved = todoRepository.save(todo);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(expandValue);
    }

    @Test
    void TODO更新できること() {
        String beforeTitle = "統合テスト：更新前のタイトル名";
        String expandTitle = "統合テスト：更新後のタイトル名";
        String expandStatus = "統合テスト：更新されたステータス名";
        Todo todo = new Todo();
        todo.setTitle(beforeTitle);
        Todo saved = todoRepository.save(todo);

        Todo updateData = new Todo();
        updateData.setTitle(expandTitle);
        updateData.setStatus(expandStatus);
        todoService.update(saved.getId(), updateData);

        Todo result = todoRepository.findById(saved.getId()).orElseThrow();
        assertThat(result.getTitle()).isEqualTo(expandTitle);
        assertThat(result.getStatus()).isEqualTo(expandStatus);
    }

    @Test
    void TODO削除できること() {
        String beforeTitle = "統合テスト：削除前のタイトル名";
        Todo todo = new Todo();
        todo.setTitle(beforeTitle);
        Todo saved = todoRepository.save(todo);

        todoRepository.deleteById(saved.getId());

        assertThat(todoRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void 存在しないIDでTODOを取得すると例外が発生すること() {
        String expandMsg = "Todoが見つかりません。";
        assertThatThrownBy(() -> todoService.findById(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(expandMsg);
    }

    @Test
    void 存在しないIDでTODOを更新すると例外が発生すること() {
        String expandMsg = "Todoが見つかりません。";
        String expandTitle = "統合テスト：更新後のタイトル名";
        String expandStatus = "統合テスト：更新されたステータス名";
        Todo updateData = new Todo();
        updateData.setTitle(expandTitle);
        updateData.setStatus(expandStatus);

        assertThatThrownBy(() -> todoService.update(999L, updateData))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(expandMsg);
    }

    @Test
    void 存在しないIDでTODOを削除すると例外が発生すること() {
        assertThatThrownBy(() -> todoService.delete(999L)).isInstanceOf(
            RuntimeException.class
        );
    }
}
