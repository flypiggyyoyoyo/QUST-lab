package com.flypiggyyoyoyo.backend.exception;

import com.flypiggyyoyoyo.backend.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobleExceptionHandler {

    @ExceptionHandler(value = DatabaseException.class)
    public Result<?> handlerDatabaseException(DatabaseException err) {
        log.error("数据库错误 {}", err.getMessage());

        return Result.DatabaseError(err.getMessage());
    }

    @ExceptionHandler(value = CodeException.class)
    public Result<?> handlerCodeException(CodeException err) {
        log.error("验证码信息错误 {}", err.getMessage());

        return Result.userError(err.getCode(),err.getMessage());
    }

    @ExceptionHandler(value = UserException.class)
    public Result<?> handlerUserException(UserException err) {
        log.error("用户信息异常 {}", err.getMessage());

        return Result.userError(err.getCode(),err.getMessage());
    }

    //捕获所有异常
    @ExceptionHandler(value = Throwable.class)
    public Result<?> handlerException(Throwable e) {
        log.error("未知错误：",e);
        return Result.ServerError(e.getMessage());
    }
}
