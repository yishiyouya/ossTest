package com.badfox.osstest.configs;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class MyOssConfig {

    /**
     * 读取配置文件的内容
     */
    @Value("${aliyun.oss.file.endpoint}")
    private String ALIYUN_OSS_ENDPOINT;
    @Value("${aliyun.oss.file.accessKeyId}")
    private String ALIYUN_OSS_ACCESSKEYID;
    @Value("${aliyun.oss.file.accessKeySecret}")
    private String ALIYUN_OSS_ACCESSKEYSECRET;
    @Value("${aliyun.oss.file.bucketName}")
    private String ALIYUN_OSS_BUCKETNAME;

    @Bean(name = "ossClient")
    @Scope("prototype")
    public OSS ossClient() {
        return new OSSClientBuilder().build(
                ALIYUN_OSS_ENDPOINT,
                ALIYUN_OSS_ACCESSKEYID,
                ALIYUN_OSS_ACCESSKEYSECRET);
    }

}
