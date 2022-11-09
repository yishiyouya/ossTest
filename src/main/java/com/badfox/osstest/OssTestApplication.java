package com.badfox.osstest;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties
@ServletComponentScan("com.badfox.osstest.filter")
@MapperScan("com.badfox.osstest.mapper")
@NacosPropertySource(dataId = "example", autoRefreshed = true)
public class OssTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssTestApplication.class, args);
    }

}
