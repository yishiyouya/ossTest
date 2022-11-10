package com.badfox.basicpro.comm;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel("业务编码")
@Getter
public enum AppCode implements StatusCode {
    APP_ERROR(2000, "业务异常"),
    PRICE_ERROR(2001, "价格异常");
    private int code;
    private String msg;

    AppCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
