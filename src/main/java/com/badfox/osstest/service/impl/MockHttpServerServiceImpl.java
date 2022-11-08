package com.badfox.osstest.service.impl;

import com.badfox.osstest.pojo.OssFileInfo;
import com.badfox.osstest.service.MockHttpServerService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 15210
 */
@Slf4j
@Service
public class MockHttpServerServiceImpl implements MockHttpServerService {

    @Override
    public String rspGetJsonRequest() throws Exception {
        OssFileInfo ossFileInfo = new OssFileInfo("mockOssFile",
                "http://localhost:8082");
        ObjectMapper objectMapper = new ObjectMapper();

        /*
        * 将对象序列化为json字符串
        * 忽略为null的字段
         */
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String resJsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ossFileInfo);
        log.info(resJsonString);
        return resJsonString;
    }

    @Override
    public String rspGetStrRequest() {
        return "http://localhost:8082";
    }
}
