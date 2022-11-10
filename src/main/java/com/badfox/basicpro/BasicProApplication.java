package com.badfox.basicpro;

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
@ServletComponentScan("com.badfox.basicpro.filter")
@MapperScan("com.badfox.basicpro.mapper")
@NacosPropertySource(dataId = "example", autoRefreshed = true)
public class BasicProApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicProApplication.class, args);
    }

}
