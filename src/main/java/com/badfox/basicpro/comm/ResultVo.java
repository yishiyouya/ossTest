package com.badfox.basicpro.comm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("通用响应对象")
@Data
public class ResultVo {
    @ApiModelProperty(value="状态码", dataType = "int")
    private int code;

    @ApiModelProperty(value="状态信息")
    private String msg;

    @ApiModelProperty("返回对象")
    private Object data;

    /**
     *  手动设置返回vo
     * @param code
     * @param msg
     * @param data
     */
    public ResultVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     *  默认返回成功状态码，数据对象
     * @param data
     */
    public ResultVo(Object data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getMsg();
        this.data = data;
    }

    /**
     *  返回指定状态码，数据对象
     * @param statusCode
     * @param data
     */
    public ResultVo(StatusCode statusCode, Object data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }

    /**
     *  只返回状态码
     * @param statusCode
     */
    public ResultVo(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = null;
    }
}