package com.flypiggyyoyoyo.demo.exception;

import com.flypiggyyoyoyo.demo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理基础业务异常
     */
    @ExceptionHandler(value = BaseBusinessException.class)
    public Result<?> handleBusinessException(BaseBusinessException ex) {
        log.error("业务异常: code={}, message={}", ex.getCode(), ex.getMessage(), ex);
        return Result.userError(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理数据库异常（可保留单独处理，添加特殊逻辑）
     */
    @ExceptionHandler(value = DatabaseException.class)
    public Result<?> handleDatabaseException(DatabaseException ex) {
        log.error("数据库操作异常: message={}", ex.getMessage(), ex);
        return Result.DatabaseError(ex.getMessage());
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<?> handleException(Throwable ex) {
        log.error("未知系统异常", ex);
        return Result.ServerError("系统繁忙，请稍后再试");
    }
}