package com.badfox.osstest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * oss存储的文件信息
 * @author 15210
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OssFileInfo {

    public OssFileInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件容量
     */
    private String fileSize;

}
