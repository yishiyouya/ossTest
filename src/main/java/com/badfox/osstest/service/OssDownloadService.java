package com.badfox.osstest.service;

/**
 * OSS 下载服务接口
 *
 * @author 15210
 */
public interface OssDownloadService {


    /**
     * 下载文件
     *
     * @param objectName
     * @param pathName   路径名，下载文件名默认同objectName中的文件名
     * @return
     */
    String downloadFile(String objectName, String pathName) throws Exception;

    /**
     * 流式下载文件
     *
     * @param objectName
     * @param pathName
     */
    void bufferDownloadFile(String objectName, String pathName) throws Exception;

    /**
     * 范围下载
     *
     * @param objectName
     * @param pathName
     * @throws Exception
     */
    void rangeDownloadFile(String objectName, String pathName) throws Exception;

    /**
     * 断点续传下载
     * 分片下载后，然后再合并
     *
     * @param objectName
     * @param pathName
     * @return
     */
    String breakPointDownloadFile(String objectName, String pathName) throws Exception;

    /**
     * 条件下载
     *
     * @param objectName
     * @param pathName
     * @return
     */
    String conditionDownloadFile(String objectName, String pathName) throws Exception;

    /**
     * 进度条下载
     *
     * @param objectName
     * @param pathName
     * @return
     * @throws Exception
     */
    String processDwnloadFile(String objectName, String pathName) throws Exception;

}
