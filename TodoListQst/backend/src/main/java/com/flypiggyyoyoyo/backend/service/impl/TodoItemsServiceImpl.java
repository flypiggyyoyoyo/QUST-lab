package com.flypiggyyoyoyo.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flypiggyyoyoyo.backend.constants.ErrorEnum;
import com.flypiggyyoyoyo.backend.data.todo.TodoCreateRequest;
import com.flypiggyyoyoyo.backend.data.todo.TodoResponse;
import com.flypiggyyoyoyo.backend.data.todo.TodoStatsResponse;
import com.flypiggyyoyoyo.backend.data.todo.TodoUpdateRequest;
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

/**
* @author flypiggy
* @description 针对表【todo_items】的数据库操作Service实现
* @createDate 2025-07-03 19:53:46
*/
@Service
public class TodoItemsServiceImpl extends ServiceImpl<TodoItemsMapper, TodoItems>
    implements TodoItemsService{

    @Override
    public TodoResponse createTodo(TodoCreateRequest request) {
        TodoItems todo = new TodoItems();
        todo.setUserId(request.getUserId());
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setStatus(request.getStatus() != null ? request.getStatus() : 0); // 默认状态0
        todo.setDueDate(request.getDueDate());
        todo.setPriority(request.getPriority() != null ? request.getPriority() : 1);
        todo.setCreationDate(new Date()); // 设置当前时间

        boolean success = this.save(todo);
        if (!success) {
            throw new TodoException(ErrorEnum.TODO_OPERATION_FAILED);
        }

        return convertToResponse(todo);
    }

    @Override
    public TodoResponse getTodo(Integer taskId) {
        TodoItems todo = this.getById(taskId);
        if (todo == null) {
            throw new TodoException(ErrorEnum.TODO_NOT_FOUND);
        }
        return convertToResponse(todo);
    }

    @Override
    public List<TodoResponse> getAllTodos() {
        List<TodoItems> todos = this.list();
        return todos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoResponse> getTodosByUserId(Integer userId) {
        QueryWrapper<TodoItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<TodoItems> todos = this.list(queryWrapper);

        return todos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TodoResponse updateTodo(Integer taskId, TodoUpdateRequest request) {
        TodoItems todo = this.getById(taskId);
        if (todo == null) {
            throw new TodoException(ErrorEnum.TODO_NOT_FOUND);
        }

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

        boolean success = this.updateById(todo);
        if (!success) {
            throw new TodoException(ErrorEnum.TODO_OPERATION_FAILED);
        }

        return convertToResponse(todo);
    }

    @Override
    public void deleteTodo(Integer taskId) {
        boolean success = this.removeById(taskId);
        if (!success) {
            throw new TodoException(ErrorEnum.TODO_OPERATION_FAILED);
        }
    }

    @Override
    public TodoResponse updateTodoStatus(Integer taskId, Integer status) {
        TodoItems todo = this.getById(taskId);
        if (todo == null) {
            throw new TodoException(ErrorEnum.TODO_NOT_FOUND);
        }

        if (status != 0 && status != 1) {
            throw new TodoException(ErrorEnum.INVALID_STATUS);
        }

        todo.setStatus(status);
        boolean success = this.updateById(todo);
        if (!success) {
            throw new TodoException(ErrorEnum.TODO_OPERATION_FAILED);
        }

        return convertToResponse(todo);
    }

    @Override
    public List<TodoResponse> filterTodos(LocalDate startDate, LocalDate endDate, Integer priority) {
        QueryWrapper<TodoItems> queryWrapper = new QueryWrapper<>();

        if (startDate != null) {
            LocalDateTime startTime = startDate.atStartOfDay();
            queryWrapper.ge("creation_date", startTime);
        }

        if (endDate != null) {
            LocalDateTime endTime = endDate.atTime(23, 59, 59);
            queryWrapper.le("creation_date", endTime);
        }

        if (priority != null) {
            queryWrapper.eq("priority", priority);
        }

        queryWrapper.orderByDesc("creation_date");

        List<TodoItems> todos = this.list(queryWrapper);
        return todos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TodoStatsResponse getTodoStats() {
        TodoStatsResponse stats = new TodoStatsResponse();

        // 1. 计算总任务数
        int totalTasks = this.count();  // MyBatis-Plus的count()方法，查询总记录数
        stats.setTotalTasks(totalTasks);

        // 2. 计算已完成任务数（假设status=1表示已完成，根据实际业务调整）
        QueryWrapper<TodoItems> completedWrapper = new QueryWrapper<>();
        completedWrapper.eq("status", 1);  // 按状态筛选已完成任务
        int completedTasks = this.count(completedWrapper);
        stats.setCompletedTasks(completedTasks);

        // 3. 计算完成占比（避免除以0的情况）
        double completionRate = 0.0;
        if (totalTasks > 0) {
            completionRate = (double) completedTasks / totalTasks * 100;
            // 保留两位小数（四舍五入）
            completionRate = Math.round(completionRate * 100) / 100.0;
        }
        stats.setCompletionRate(completionRate);

        return stats;
    }

    private TodoResponse convertToResponse(TodoItems todo) {
        TodoResponse response = new TodoResponse();
        response.setTaskId(todo.getTaskId());
        response.setUserId(todo.getUserId());
        response.setTitle(todo.getTitle());
        response.setDescription(todo.getDescription());
        response.setStatus(todo.getStatus());
        response.setDueDate(todo.getDueDate());
        response.setPriority(todo.getPriority());
        response.setCreationDate(todo.getCreationDate());
        return response;
    }
}




