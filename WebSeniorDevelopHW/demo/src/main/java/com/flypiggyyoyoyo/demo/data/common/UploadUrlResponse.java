package com.flypiggyyoyoyo.demo.data.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)

public class UploadUrlResponse {
    private String uploadUrl;
    private String downloadUrl;
}
