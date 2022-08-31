package com.badfox.osstest.util;

import com.badfox.osstest.configs.MyOssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

/**
 * 加解密 工具类
 */
@Slf4j
@Component
public class MyOSSUtil {

    /**
     * 解密
     * @param str
     * @return
     * @throws Exception
     */
    public String decryptStr(String str) throws Exception {
        String result = "";
        result = new String(Base64.getDecoder().decode(str), "UTF-8");
        return result;
    }


}
