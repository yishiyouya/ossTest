package com.badfox.osstest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 模拟提供 HTTP 服务，支持Get请求，可返回 json,String
 * @author 15210
 */
public interface MockHttpServerService {


    String rspGetJsonRequest() throws Exception;

    String rspGetStrRequest();


}
