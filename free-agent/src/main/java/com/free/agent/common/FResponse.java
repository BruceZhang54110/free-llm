package com.free.agent.common;

import java.io.Serializable;

public class FResponse<T> implements Serializable {
    /**
     * 业务状态码
     */
    private int code;

    /**
     *  信息描述
     */
    private String message;


    /**
     * 数据
     */
    private T data;

    public FResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public FResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
