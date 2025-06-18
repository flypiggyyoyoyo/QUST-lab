package com.flypiggyyoyoyo.demo.utils;

import cn.hutool.core.util.StrUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * OSSUtils 类用于处理与 MinIO 对象存储服务相关的操作，
 * 提供了文件上传预签名 URL 的生成和文件下载 URL 的生成功能。
 */
@Service
public class OSSUtils {

    // 注入 MinioClient 实例，用于与 MinIO 对象存储服务进行交互
    @Resource
    private MinioClient minioClient;

    //从配置文件中读取 MinIO 服务的基础 URL，用于构建下载 URL
    @Value("${minio.url}")
    private String url;

    /**
     * 生成用于上传文件的预签名 URL。
     *
     * @param bucketName 存储桶名称，指定文件要上传到的存储桶。
     * @param objectName 对象名称，即上传到存储桶后的文件名。
     * @param expires    预签名 URL 的有效时间，单位为秒。
     * @return 生成的预签名 URL，可用于客户端直接上传文件到 MinIO 存储桶。
     * @throws Exception 当生成预签名 URL 过程中出现异常时抛出。
     *                   使用 @SneakyThrows 注解，可避免在方法签名中显式声明异常。
     */
    @SneakyThrows
    public String uploadFile(String bucketName, String objectName, Integer expires) {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expires, TimeUnit.SECONDS)
                        .build());
    }

    /**
     * 生成文件的下载 URL。
     *
     * @param bucketName 存储桶名称，指定文件所在的存储桶。
     * @param fileName   文件名，即存储在存储桶中的对象名称。
     * @return 生成的文件下载 URL，客户端可通过该 URL 下载文件。
     */
    public String downUrl(String bucketName, String fileName) {

        return url + StrUtil.SLASH + bucketName + StrUtil.SLASH + fileName;
    }
}
