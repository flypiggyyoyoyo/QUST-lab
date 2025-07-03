package com.flypiggyyoyoyo.backend.data.todo;

import lombok.Data;
import java.util.Date;

@Data
public class TodoUpdateRequest {
    private String title;
    private String description;
    private Integer status;
    private Date dueDate;
    private Integer priority;
}