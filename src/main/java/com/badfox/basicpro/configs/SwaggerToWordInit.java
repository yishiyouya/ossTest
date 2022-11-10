package com.badfox.basicpro.configs;

import com.github.lfopenjavaswagger2word.util.GenerateDocxUtils;
import com.github.lfopenjavaswagger2word.util.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Slf4j
public class SwaggerToWordInit implements ApplicationRunner {

    private static final String URL_DOC = "/v2/api-docs";

    @Value("${server.port}")
    private String port;
    @Value("${spring.application.name}")
    private String proName;

    public OutputStream getOutPutStream(String path, String fileName) throws FileNotFoundException {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        File outFile = new File(path+File.separator+fileName);
        FileOutputStream outputStream = new FileOutputStream(outFile);
        return outputStream;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String uri = "http://127.0.0.1:" + port + URL_DOC;
        log.info("访问：{}", uri);
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        OutputStream outputStream = null;
        String result = "";
        try {
            response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
                outputStream = getOutPutStream("docs", proName+".doc");

                boolean b = GenerateDocxUtils.generateFileByJSON(result,
                        outputStream);
                log.info("结果：{}", result != null ? "success" : "fail!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != outputStream) {
                    response.close();
                }
                if (null != outputStream) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
