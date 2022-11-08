package com.badfox.osstest.service.impl;

import com.badfox.osstest.pojo.OssFileInfo;
import com.badfox.osstest.service.OssHttpClientService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;


/**
 * @author 15210
 */
@Slf4j
@Service
public class OssHttpClientServiceImpl implements OssHttpClientService {

    @Override
    public String getRequest() throws Exception {
        String result = "";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String uri = "http://localhost:8082/mockGetJson";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        result = response.getBody();
        return result;
    }

    @Override
    public String getRequest(String url) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        OssFileInfo ossFileInfo = new OssFileInfo();
        ossFileInfo.setUrl(url);
        ossFileInfo.setFileSize("10MB");
        ossFileInfo.setContentType("txt");
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
    public String postRequest() throws Exception {
        String uri = "https://www.baidu.com";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user", "你好");
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
