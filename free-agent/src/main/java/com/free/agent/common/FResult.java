package com.free.agent.common;

public class FResult {


    /**
     * 返回成功响应信息
     * @param data
     * @return
     * @param <T>
     */
    public static <T> FResponse<T> success(T data) {
        FResponse<T> response = new FResponse<>(ResultCode.SUCCESS.getCode()
                , ResultCode.SUCCESS.getMessage()
                , data);
        return response;
    }


    /**
     * 系统异常
     * @return
     */
    public static FResponse<?> fail() {
        return new FResponse<>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage());
    }

    /**
     * 业务异常
     * @param resultCode
     * @return
     */
    public static FResponse<?> businessFail(ResultCode resultCode) {
        return new FResponse<>(resultCode.getCode(), resultCode.getMessage());
    }

    public static FResponse<?> businessFail(String message) {
        return new FResponse<>(ResultCode.ERROR.getCode(), message);
    }

    public static FResponse<?> paramError(ResultCode resultCode, String message) {
        return new FResponse<>(resultCode.getCode(), message);
    }

    public static FResponse<?> paramError(ResultCode resultCode) {
        return new FResponse<>(resultCode.getCode(), resultCode.getMessage());
    }
}
