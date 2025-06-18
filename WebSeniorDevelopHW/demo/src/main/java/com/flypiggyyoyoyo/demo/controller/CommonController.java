package com.flypiggyyoyoyo.demo.controller;

import com.flypiggyyoyoyo.demo.data.common.UploadUrlRequest;
import com.flypiggyyoyoyo.demo.data.common.UploadUrlResponse;
import com.flypiggyyoyoyo.demo.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     * 客户端请求 presigned 上传 URL
     * POST /uploadUrl
     * 请求体: { "fileName": "xxx.png" }
     * 返回: { "uploadUrl": "...", "downloadUrl": "..." }
     */
    @PostMapping("/uploadUrl")
    public UploadUrlResponse uploadUrl(
            @Valid @RequestBody UploadUrlRequest request
    ) throws Exception {
        return commonService.uploadUrl(request);
    }
}
