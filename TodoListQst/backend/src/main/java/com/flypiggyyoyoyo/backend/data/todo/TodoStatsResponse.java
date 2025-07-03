// src/main/java/com/flypiggyyoyoyo/backend/data/todo/TodoStatsResponse.java
package com.flypiggyyoyoyo.backend.data.todo;

import lombok.Data;

@Data
public class TodoStatsResponse {
    private int totalTasks;         // 总任务数
    private int completedTasks;     // 已完成任务数
    private double completionRate;  // 完成占比（百分比，保留两位小数）
}