package com.badfox.osstest.comm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("通用请求类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReqInfo {
    @ApiModelProperty("当前页数")
    @Min(value = 1, message = "当前页数必须大于或等于1")
    private int curr = 1;
    @ApiModelProperty("查询记录数")
    @Min(value = 0, message = "查询记录树需大于或等于0")
    private int size = 10;
    private String code;
}
