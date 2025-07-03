package com.flypiggyyoyoyo.backend.constants;

import lombok.Getter;

@Getter
public enum ErrorEnum {
    // 通用成功状态
    SUCCESS(200, "OK", 200),

    // 注册相关错误（客户端参数问题）
    REGISTER_ERROR(40001, "注册失败，用户已存在", 400),  // 400：客户端参数错误

    // 登录/认证相关错误（认证失败）
    LOGIN_USER_NOT_FOUND(40101, "登录失败，用户不存在", 401),  // 401：未认证
    LOGIN_PASSWORD_ERROR(40102, "登录失败，密码错误", 401),
    TOKEN_EXPIRED(40103, "令牌已过期，请重新登录", 401),       // 新增：JWT过期
    INVALID_TOKEN(40104, "无效的令牌", 401),                 // 新增：令牌无效

    // 权限相关错误
    ILLEGAL_SOURCE(40301, "非法请求来源", 403),              // 403：禁止访问
    TODO_PERMISSION_DENIED(40302, "无权限操作该待办事项", 403),

    // 资源不存在错误
    TODO_NOT_FOUND(40401, "待办事项不存在", 404),             // 404：资源不存在

    // 客户端参数错误
    INVALID_STATUS(40002, "无效的状态值（仅支持0或1）", 400),  // 400：参数无效
    INVALID_DATE_FORMAT(40003, "日期格式错误（应为yyyy-MM-dd）", 400),  // 新增：日期格式错误

    // 服务器内部错误
    DATABASE_ERROR(50001, "数据库操作失败", 500),
    TODO_OPERATION_FAILED(50002, "待办事项操作失败", 500),
    SYSTEM_ERROR(50003, "系统内部错误", 500);  // 新增：通用系统错误


    private final int code;         // 业务错误码（自定义）
    private final String message;   // 错误描述
    private final int httpStatus;   // HTTP状态码（符合规范）

    // 构造函数：显式指定HTTP状态码（推荐优先使用）
    ErrorEnum(int code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    // 兼容旧构造（不推荐再使用，避免状态码混乱）
    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
        // 仅作为兼容，新添加的错误必须显式指定httpStatus
        this.httpStatus = 500;
    }
}