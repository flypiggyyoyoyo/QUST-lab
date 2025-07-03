package com.flypiggyyoyoyo.backend.data.todo;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class TodoCreateRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    private String description;
    private Integer status;
    private Date dueDate;
    private Integer priority;
}