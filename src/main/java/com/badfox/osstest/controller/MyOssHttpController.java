package com.badfox.osstest.controller;

import com.badfox.osstest.pojo.OssFileInfo;
import com.badfox.osstest.service.MockHttpServerService;
import com.badfox.osstest.service.OssHttpClientService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MyOssHttpController {

    @Autowired
    private OssHttpClientService ossHttpClientService;
    @Autowired
    private MockHttpServerService mockHttpServerService;


    @RequestMapping("/ossGetDown")
    public String rspGetDownRequest() throws Exception {
        String result = "";
        String queryResult = rspGetRequest();

        if (StringUtils.hasText(queryResult)) {
            ObjectMapper objectMapper = new ObjectMapper();
            //将json反序列化为java对象
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            OssFileInfo ossFileInfo = objectMapper.readValue(queryResult, OssFileInfo.class);

            //使用此url，下载文件
            String url = ossFileInfo.getUrl();
            result = ossHttpClientService.getRequest(url);
        } else {
            log.error("ossGetDown's rspGetRequest no response.");
        }

        return result;
    }

    @RequestMapping("/ossGet")
    public String rspGetRequest() throws Exception {
        String result = "";
        result = ossHttpClientService.getRequest();


        return result;
    }

    @RequestMapping("/ossDown")
    public String rspGetOssDownRequest(@RequestParam(value = "url", required = false) String url) throws Exception {
        String result = "";
        result = ossHttpClientService.getRequest(url);
        return result;
    }

    @RequestMapping("/ossPost")
    public String rspPostRequest() throws Exception {
        String result = "";
        result = ossHttpClientService.postRequest();
        return result;
    }

    @RequestMapping("/mockGetJson")
    public String rspMockGetJson() throws Exception {
        String result = "";
        result = mockHttpServerService.rspGetJsonRequest();
        return result;
    }

    @RequestMapping("/mockGetStr")
    public String rspMockGetStr() throws Exception {
        String result = "";
        result = mockHttpServerService.rspGetStrRequest();
        return result;
    }





}
