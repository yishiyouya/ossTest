package com.badfox.basicpro.comm;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private int code;
    private String msg;


    public BaseException(ResultCode responsePackError, String message) {
        super(message);
        this.code = responsePackError.getCode();
        this.msg = responsePackError.getMsg();
    }

    public BaseException(String errorMsg) {
        super(errorMsg);
        this.code = AppCode.APP_ERROR.getCode();
        this.msg = AppCode.APP_ERROR.getMsg();
    }

}
