package com.badfox.osstest.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("dept")
public class DeptPO extends Model<DeptPO> {
    @TableId("id")
    private String id;
    @TableField("code")
    private String code;
    @TableField("name")
    private String name;
    @TableField("parent_code")
    private String parentCode;
    @TableField(value = "del_flag", select = false)
    @TableLogic
    private String delFlag;
    @TableField(value = "crt_time", fill = FieldFill.INSERT)
    private LocalDateTime crtTime;
    @TableField(value = "upd_time", fill = FieldFill.UPDATE)
    private LocalDateTime updTime;
}
