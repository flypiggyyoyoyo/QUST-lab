package com.flypiggyyoyoyo.backend.data.todo;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TodoFilterRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer priority;
}