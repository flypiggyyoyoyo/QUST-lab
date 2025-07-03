package com.flypiggyyoyoyo.backend.data.todo;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class StatusUpdateRequest {
    @NotNull(message = "状态不能为空")
    private Integer status;
}