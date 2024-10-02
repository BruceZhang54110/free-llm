package com.free.agent.common.ex;

import com.free.agent.common.ResultCode;

public class BusinessException extends RuntimeException {

    private ResultCode resultCode;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }


    public BusinessException(Throwable cause) {
        super(cause);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
