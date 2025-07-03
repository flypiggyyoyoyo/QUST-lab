package com.flypiggyyoyoyo.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.backend.constants.ErrorEnum;
import com.flypiggyyoyoyo.backend.data.todo.*;
import com.flypiggyyoyoyo.backend.exception.TodoException;
import com.flypiggyyoyoyo.backend.model.TodoItems;
import com.flypiggyyoyoyo.backend.service.TodoItemsService;
import com.flypiggyyoyoyo.backend.mapper.TodoItemsMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoItemsServiceImpl extends ServiceImpl<TodoItemsMapper, TodoItems>
        implements TodoItemsService {

    @Override
    public TodoResponse createTodo(Integer userId, TodoCreateRequest request) {
        TodoItems todo = new TodoItems();
        todo.setUserId(userId); // 使用Token提供的用户ID
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setStatus(request.getStatus() != null ? request.getStatus() : 0);
        todo.setDueDate(request.getDueDate());
        todo.setPriority(request.getPriority() != null ? request.getPriority() : 1);
        todo.setCreationDate(new Date());

        if (!this.save(todo)) {
            throw new TodoException(ErrorEnum.TODO_OPERATION_FAILED);
        }

        return convertToResponse(todo);
    }

    @Override
    public TodoResponse getTodo(Integer userId, Integer taskId) {
        TodoItems todo = this.getOne(new QueryWrapper<TodoItems>()
                .eq("task_id", taskId)
                .eq("user_id", userId));

        if (todo == null) {
            throw new TodoException(ErrorEnum.TODO_NOT_FOUND);
        }
        return convertToResponse(todo);
    }

    @Override
    public List<TodoResponse> getAllTodos(Integer userId) {
        List<TodoItems> todos = this.list(new QueryWrapper<TodoItems>()
                .eq("user_id", userId));

        return todos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TodoResponse updateTodo(Integer userId, Integer taskId, TodoUpdateRequest request) {
        TodoItems todo = this.getOne(new QueryWrapper<TodoItems>()
                .eq("task_id", taskId)
                .eq("user_id", userId));

        if (todo == null) {
            throw new TodoException(ErrorEnum.TODO_NOT_FOUND);
        }

        updateTodoFields(todo, request);

        if (!this.updateById(todo)) {
            throw new TodoException(ErrorEnum.TODO_OPERATION_FAILED);
        }

        return convertToResponse(todo);
    }

    @Override
    public void deleteTodo(Integer userId, Integer taskId) {
        boolean exists = this.count(new QueryWrapper<TodoItems>()
                .eq("task_id", taskId)
                .eq("user_id", userId)) > 0;

        if (!exists) {
            throw new TodoException(ErrorEnum.TODO_NOT_FOUND);
        }

        if (!this.removeById(taskId)) {
            throw new TodoException(ErrorEnum.TODO_OPERATION_FAILED);
        }
    }

    @Override
    public TodoResponse updateTodoStatus(Integer userId, Integer taskId, Integer status) {
        TodoItems todo = this.getOne(new QueryWrapper<TodoItems>()
                .eq("task_id", taskId)
                .eq("user_id", userId));

        if (todo == null) {
            throw new TodoException(ErrorEnum.TODO_NOT_FOUND);
        }

        if (status != 0 && status != 1) {
            throw new TodoException(ErrorEnum.INVALID_STATUS);
        }

        todo.setStatus(status);
        if (!this.updateById(todo)) {
            throw new TodoException(ErrorEnum.TODO_OPERATION_FAILED);
        }

        return convertToResponse(todo);
    }

    @Override
    public List<TodoResponse> filterTodos(
            Integer userId,
            LocalDate startDate,
            LocalDate endDate,
            Integer priority
    ) {
        QueryWrapper<TodoItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        if (startDate != null) {
            queryWrapper.ge("due_date", startDate);
        }

        if (endDate != null) {
            queryWrapper.le("due_date", endDate);
        }

        if (priority != null) {
            queryWrapper.eq("priority", priority);
        }

        return this.list(queryWrapper).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TodoStatsResponse getTodoStatsByUserId(Integer userId) {
        // 创建基础查询条件（筛选当前用户的任务）
        QueryWrapper<TodoItems> baseWrapper = new QueryWrapper<>();
        baseWrapper.eq("user_id", userId);

        // 1. 计算总任务数
        int totalTasks = this.count(baseWrapper);

        // 2. 计算已完成任务数（复制基础查询条件，再添加状态筛选）
        QueryWrapper<TodoItems> completedWrapper = baseWrapper.clone(); // 使用clone()复制条件
        completedWrapper.eq("status", 1); // 已完成状态（假设1是已完成）
        int completedTasks = this.count(completedWrapper);

        // 3. 计算完成率（保留两位小数）
        double completionRate = totalTasks > 0 ?
                Math.round((double) completedTasks / totalTasks * 10000) / 100.0 : 0.0;

        // 注意：确保TodoStatsResponse有对应的构造函数，或使用setter赋值
        return new TodoStatsResponse(totalTasks, completedTasks, completionRate);
    }

    private void updateTodoFields(TodoItems todo, TodoUpdateRequest request) {
        if (request.getTitle() != null) {
            todo.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            todo.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            todo.setStatus(request.getStatus());
        }
        if (request.getDueDate() != null) {
            todo.setDueDate(request.getDueDate());
        }
        if (request.getPriority() != null) {
            todo.setPriority(request.getPriority());
        }
    }

    private TodoResponse convertToResponse(TodoItems todo) {
        return TodoResponse.builder()
                .taskId(todo.getTaskId())
                .userId(todo.getUserId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .status(todo.getStatus())
                .dueDate(todo.getDueDate())
                .priority(todo.getPriority())
                .creationDate(todo.getCreationDate())
                .build();
    }
}