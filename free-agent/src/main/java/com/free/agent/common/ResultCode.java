package com.free.agent.common;

/**
 * Copyright:©2022,北京人民在线网络有限公司
 * @Author: zhanghongwei
 * @Date: 2024-08-09 11:17
 * @Description: 全局结果code
 * 200,400,500 为系统异常code
 * 1000 以上为 业务异常code
 **/
public enum ResultCode {
    SUCCESS(200, "success"),

    PARAM_ERROR(400, "param error"),

    ERROR(500, "server error"),

    USER_NAME_PASSWORD_ERROR(1000, "用户名或密码错误"),
    USER_LOGIN_EXPIRE(1200, "用户登录已过期"),

    /**
     * 重置密码时，新旧密码不同
     */
    PASSWORD_ERROR(1300, "请输入正确密码"),

    REGISTER_PHONE_EXISTS(1310, "该手机号已被注册"),
    REGISTER_CREDIT_CODE_EXISTS(1311, "统一社会信用代码已存在"),
    REGISTER_CREDIT_CODE_NOT_EXISTS(1320, "统一社会信用代码在企业库中未找到");

    private int code;

    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
