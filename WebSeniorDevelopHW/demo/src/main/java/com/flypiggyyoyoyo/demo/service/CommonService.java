package com.flypiggyyoyoyo.demo.service;

import com.flypiggyyoyoyo.demo.data.common.UploadUrlRequest;
import com.flypiggyyoyoyo.demo.data.common.UploadUrlResponse;

public interface CommonService {
    UploadUrlResponse uploadUrl(UploadUrlRequest request) throws Exception;
}
