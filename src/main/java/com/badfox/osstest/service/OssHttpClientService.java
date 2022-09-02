package com.badfox.osstest.service;

import com.google.gson.JsonObject;

/**
 * 模仿HTTP请求
 * @author 15210
 */
public interface OssHttpClientService {

    public String getRequest() throws Exception;
    public String getRequest(String url) throws Exception;

    public String postRequest() throws Exception;

}
