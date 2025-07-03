package com.flypiggyyoyoyo.backend.common;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * 统一API响应结果封装
 */
@Data
@Accessors(chain = true)
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    public static <T> Result<T> OK(T data) {
        Result<T> result = new Result<>();
        return result.setCode(HttpStatus.OK.value())
                .setData(data);
    }

    public static <T> Result<T> DatabaseError(String msg) {
        Result<T> result = new Result<>();
        return result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMsg(msg);
    }

    public static <T> Result<T> ServerError(String msg){
        Result<T> result = new Result<>();
        return result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMsg(msg);
    }

    public static <T> Result<T> userError(int code,String msg) {
        Result<T> r = new Result<>();

        return r.setCode(code).setMsg(msg);
    }
}
