package com.badfox.osstest.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel("dept-机构表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("dept")
public class DeptPO extends Model<DeptPO> {
    @NotNull(message="id不能为空")
    @TableId("id")
    private String id;

    @ApiModelProperty("编码")
    @TableField("code")
    private String code;

    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("父级编码")
    @TableField("parent_code")
    private String parentCode;

    @ApiModelProperty("删除标志")
    @TableField(value = "del_flag", select = false)
    @TableLogic
    private String delFlag;

    @ApiModelProperty(value = "新建时间", dataType = "LocalDateTime")
    @TableField(value = "crt_time", fill = FieldFill.INSERT)
    private LocalDateTime crtTime;

    @ApiModelProperty(value = "更新时间", dataType = "LocalDateTime")
    @TableField(value = "upd_time", fill = FieldFill.UPDATE)
    private LocalDateTime updTime;
}
