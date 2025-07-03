package com.flypiggyyoyoyo.backend.controller;

import com.flypiggyyoyoyo.backend.common.Result;
import com.flypiggyyoyoyo.backend.data.todo.*;
import com.flypiggyyoyoyo.backend.service.TodoItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    @Autowired
    private TodoItemsService todoService;

    /**
     * 创建新的待办事项
     */
    @PostMapping
    public Result<TodoResponse> createTodo(@Valid @RequestBody TodoCreateRequest request) {
        TodoResponse response = todoService.createTodo(request);
        return Result.OK(response);
    }

    /**
     * 获取单个待办事项
     */
    @GetMapping("/{id}")
    public Result<TodoResponse> getTodo(@PathVariable int id) {
        TodoResponse response = todoService.getTodo(id);
        return Result.OK(response);
    }

    /**
     * 获取所有待办事项列表
     */
    @GetMapping
    public Result<List<TodoResponse>> getAllTodos() {
        List<TodoResponse> response = todoService.getAllTodos();
        return Result.OK(response);
    }

    /**
     * 更新待办事项
     */
    @PutMapping("/{id}")
    public Result<TodoResponse> updateTodo(@PathVariable int id,
                                           @Valid @RequestBody TodoUpdateRequest request) {
        TodoResponse response = todoService.updateTodo(id, request);
        return Result.OK(response);
    }

    /**
     * 删除待办事项
     */
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deleteTodo(@PathVariable int id) {
        todoService.deleteTodo(id);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "删除成功");

        return Result.OK(result);
    }

    /**
     * 更新待办事项状态
     */
    @PatchMapping("/{id}/status")
    public Result<TodoResponse> updateTodoStatus(
            @PathVariable int id,
            @RequestBody @Valid StatusUpdateRequest request
    ) {
        TodoResponse response = todoService.updateTodoStatus(id, request.getStatus());
        return Result.OK(response);
    }

    /**
     * 筛选待办事项
     */
    @GetMapping("/filter")
    public Result<List<TodoResponse>> filterTodos(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")  // 新增：指定日期格式
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")  // 新增：指定日期格式
            LocalDate endDate,

            @RequestParam(required = false)
            Integer priority
    ) {
        List<TodoResponse> response = todoService.filterTodos(startDate, endDate, priority);
        return Result.OK(response);
    }

    /**
     * 获取任务统计数据（总任务数、已完成数、完成占比）
     */
    @GetMapping("/stats")
    public Result<TodoStatsResponse> getTodoStats() {
        TodoStatsResponse stats = todoService.getTodoStats();
        return Result.OK(stats);
    }
}