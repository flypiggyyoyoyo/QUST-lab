package com.flypiggyyoyoyo.backend.data.todo;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder // 添加 Builder 注解
public class TodoResponse {
    private Integer taskId;
    private Integer userId;
    private String title;
    private String description;
    private Integer status;
    private Date dueDate;
    private Integer priority;
    private Date creationDate;
}