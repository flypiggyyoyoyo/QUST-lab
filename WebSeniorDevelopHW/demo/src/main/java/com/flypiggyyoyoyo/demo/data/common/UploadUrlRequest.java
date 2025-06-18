package com.flypiggyyoyoyo.demo.data.common;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)

public class UploadUrlRequest {
    @NotEmpty(message="文件名不能为空")
    private String fileName;
}
