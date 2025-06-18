package com.flypiggyyoyoyo.demo.service.impl;

import com.flypiggyyoyoyo.demo.constants.OSSConstant;
import com.flypiggyyoyoyo.demo.data.common.UploadUrlRequest;
import com.flypiggyyoyoyo.demo.data.common.UploadUrlResponse;
import com.flypiggyyoyoyo.demo.service.CommonService;
import com.flypiggyyoyoyo.demo.utils.OSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private OSSUtils ossUtils;

    @Override
    public UploadUrlResponse uploadUrl(UploadUrlRequest request) throws Exception {
        String fileName = request.getFileName();

        String uploadUrl = ossUtils.uploadFile(OSSConstant.BUCKET_NAME,fileName,OSSConstant.PICTURE_EXPIRE_TIME);
        String downUrl = ossUtils.downUrl(OSSConstant.BUCKET_NAME,fileName);

        log.debug("上传 presignedUrl = {}, 下载 downloadUrl = {}", uploadUrl, downUrl);

        UploadUrlResponse response = new UploadUrlResponse();
        response.setUploadUrl(uploadUrl)
                .setDownloadUrl(downUrl);

        return response;
    }
}
