package com.badfox.osstest.configs;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.badfox.osstest.util.MyOSSUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class MyOssConfig {

    @Autowired
    private MyOSSUtil myOSSUtil;

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


    /**
     * 默认下载限速 (KB)
     */
    private final int DEFAULT_OSS_DOWN_LIMIT_SPEED = 1000;
    private final int DEFAULT_OSS_DOWN_LIMIT_SPEED_MIN = 30;
    private final int DEFAULT_OSS_DOWN_LIMIT_SPEED_MAX = 1000 * 1024;


    /**
     * 解密配置文件
     * @throws Exception
     */
    @PostConstruct
    public void initOSSArgs() throws Throwable {
        this.setALIYUN_OSS_ACCESSKEYID(myOSSUtil.decryptStr(ALIYUN_OSS_ACCESSKEYID));
        this.setALIYUN_OSS_ACCESSKEYSECRET(myOSSUtil.decryptStr(ALIYUN_OSS_ACCESSKEYSECRET));
    }

    @Bean(name = "ossClient")
    @Scope("prototype")
    public OSS ossClient() {
        return new OSSClientBuilder().build(
                ALIYUN_OSS_ENDPOINT,
                ALIYUN_OSS_ACCESSKEYID,
                ALIYUN_OSS_ACCESSKEYSECRET);
    }

    /**
     * 默认 1000 kb/s
     * [30k/s, 1000MB/s]
     * @param limitSpeed
     * @return
     */
    public Integer getLimitSpeed(Integer limitSpeed) {
        Integer result = this.getDEFAULT_OSS_DOWN_LIMIT_SPEED();
        if (limitSpeed == null) {
            result = this.getDEFAULT_OSS_DOWN_LIMIT_SPEED();
        } else if (limitSpeed <= this.getDEFAULT_OSS_DOWN_LIMIT_SPEED_MIN()){
            result = this.getDEFAULT_OSS_DOWN_LIMIT_SPEED_MIN();
        } else if (limitSpeed >= this.getDEFAULT_OSS_DOWN_LIMIT_SPEED_MAX()){
            result = this.getDEFAULT_OSS_DOWN_LIMIT_SPEED_MAX();
        } else {
            result = limitSpeed;
        }
        return result;
    }

}
