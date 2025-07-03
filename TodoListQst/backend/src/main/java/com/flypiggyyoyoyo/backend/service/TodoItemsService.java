package com.flypiggyyoyoyo.backend.service;

import com.flypiggyyoyoyo.backend.data.todo.TodoCreateRequest;
import com.flypiggyyoyoyo.backend.data.todo.TodoResponse;
import com.flypiggyyoyoyo.backend.data.todo.TodoStatsResponse;
import com.flypiggyyoyoyo.backend.data.todo.TodoUpdateRequest;
import com.flypiggyyoyoyo.backend.model.TodoItems;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
* @author flypiggy
* @description 针对表【todo_items】的数据库操作Service
* @createDate 2025-07-03 19:53:46
*/
public interface TodoItemsService extends IService<TodoItems> {
    TodoResponse createTodo(Integer userId, TodoCreateRequest request);

    TodoResponse getTodo(Integer userId, Integer taskId);

    List<TodoResponse> getAllTodos(Integer userId);

    TodoResponse updateTodo(Integer userId, Integer taskId, TodoUpdateRequest request);

    void deleteTodo(Integer userId, Integer taskId);

    TodoResponse updateTodoStatus(Integer userId, Integer taskId, Integer status);

    List<TodoResponse> filterTodos(
            Integer userId,
            LocalDate startDate,
            LocalDate endDate,
            Integer priority
    );

    TodoStatsResponse getTodoStatsByUserId(Integer userId);
}
