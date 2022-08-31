package com.badfox.osstest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ServletComponentScan("com.badfox.osstest.filter")
public class OssTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssTestApplication.class, args);
    }

}
