package com.badfox.basicpro.configs;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AutoFillHandler implements MetaObjectHandler {

    private final String CRT_TIME = "crtTime";
    private final String UPD_TIME = "updTime";
    @Override
    public void insertFill(MetaObject metaObject) {
        Object val = getFieldValByName(CRT_TIME, metaObject);
        if (metaObject.hasSetter(CRT_TIME)
            && null == val) {
            setFieldValByName(CRT_TIME, LocalDateTime.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object val = getFieldValByName(UPD_TIME, metaObject);
        if (metaObject.hasSetter(UPD_TIME)
                && null == val) {
            setFieldValByName(UPD_TIME, LocalDateTime.now(), metaObject);
        }
    }
}
