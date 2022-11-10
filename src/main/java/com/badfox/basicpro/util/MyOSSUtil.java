package com.badfox.basicpro.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 加解密 工具类
 */
@Slf4j
@Component
public class MyOSSUtil {

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("a", false);

    }

    /**
     * 解密
     * @param str
     * @return
     * @throws Throwable
     */
    public String decryptStr(String str) throws Throwable {
        String result = "";
        result = new String(Base64.getDecoder().decode(str), "UTF-8");
        return result;
    }


}
