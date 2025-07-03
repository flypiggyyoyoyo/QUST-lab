package com.flypiggyyoyoyo.backend.controller;

import com.flypiggyyoyoyo.backend.common.Result;
import com.flypiggyyoyoyo.backend.data.todo.*;
import com.flypiggyyoyoyo.backend.service.TodoItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
     * 创建新的待办事项（关联当前登录用户）
     */
    @PostMapping
    public Result<TodoResponse> createTodo(
            HttpServletRequest request,  // 用于获取当前用户ID
            @Valid @RequestBody TodoCreateRequest requestBody
    ) {
        // 从请求属性中获取当前登录用户ID（由JWT拦截器设置）
        Integer userId = (Integer) request.getAttribute("userId");
        // 调用服务层方法，传入用户ID，确保任务关联到该用户
        TodoResponse response = todoService.createTodo(userId, requestBody);
        return Result.OK(response);
    }

    /**
     * 获取单个待办事项（仅允许获取当前用户的任务）
     */
    @GetMapping("/{id}")
    public Result<TodoResponse> getTodo(
            HttpServletRequest request,
            @PathVariable int id
    ) {
        Integer userId = (Integer) request.getAttribute("userId");
        // 服务层需验证任务属于当前用户
        TodoResponse response = todoService.getTodo(userId, id);
        return Result.OK(response);
    }

    /**
     * 获取所有待办事项列表（仅返回当前用户的任务）
     */
    @GetMapping
    public Result<List<TodoResponse>> getAllTodos(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        // 服务层根据用户ID筛选任务
        List<TodoResponse> response = todoService.getAllTodos(userId);
        return Result.OK(response);
    }

    /**
     * 更新待办事项（仅允许更新当前用户的任务）
     */
    @PutMapping("/{id}")
    public Result<TodoResponse> updateTodo(
            HttpServletRequest request,
            @PathVariable int id,
            @Valid @RequestBody TodoUpdateRequest requestBody
    ) {
        Integer userId = (Integer) request.getAttribute("userId");
        // 服务层验证任务属于当前用户后再更新
        TodoResponse response = todoService.updateTodo(userId, id, requestBody);
        return Result.OK(response);
    }

    /**
     * 删除待办事项（仅允许删除当前用户的任务）
     */
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deleteTodo(
            HttpServletRequest request,
            @PathVariable int id
    ) {
        Integer userId = (Integer) request.getAttribute("userId");
        // 服务层验证任务属于当前用户后再删除
        todoService.deleteTodo(userId, id);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "删除成功");

        return Result.OK(result);
    }

    /**
     * 更新待办事项状态（仅允许更新当前用户的任务）
     */
    @PatchMapping("/{id}/status")
    public Result<TodoResponse> updateTodoStatus(
            HttpServletRequest request,
            @PathVariable int id,
            @RequestBody @Valid StatusUpdateRequest requestBody
    ) {
        Integer userId = (Integer) request.getAttribute("userId");
        // 服务层验证任务属于当前用户后再更新状态
        TodoResponse response = todoService.updateTodoStatus(userId, id, requestBody.getStatus());
        return Result.OK(response);
    }

    /**
     * 筛选待办事项（仅筛选当前用户的任务）
     */
    @GetMapping("/filter")
    public Result<List<TodoResponse>> filterTodos(
            HttpServletRequest request,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate endDate,
            @RequestParam(required = false)
            Integer priority
    ) {
        Integer userId = (Integer) request.getAttribute("userId");
        // 服务层根据用户ID+筛选条件查询
        List<TodoResponse> response = todoService.filterTodos(userId, startDate, endDate, priority);
        return Result.OK(response);
    }

    /**
     * 获取任务统计数据（仅统计当前用户的任务）
     */
    @GetMapping("/stats")
    public Result<TodoStatsResponse> getTodoStats(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        // 服务层统计当前用户的任务数据
        TodoStatsResponse stats = todoService.getTodoStatsByUserId(userId);
        return Result.OK(stats);
    }
}