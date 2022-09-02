package com.badfox.osstest.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 15210
 * OSS上传/下载/访问文件时是否有带宽、并发连接数、QPS限制
 * OSS不支持单独调整带宽和并发、QPS限制。
 * 同一账号在同一地域内的上传或下载的带宽和请求的QPS缺省阈值为：
 * 带宽：中国内地各地域10 Gbit/s。（上传一般取决于客户端本地网络质量）
 * 其他地域 5 Gbit/s。
 * QPS：10000次/s。
 *
 * 限速：[100kb/s, 1000 Mb/s]
 */
public interface OssUploadService {

    /**
     * 简单上传文件
     *
     * @param objectName
     * @param filePath
     * @return
     */
    String upload(String objectName, String filePath);

    /**
     * 追加上传
     * filePath 文件夹名
     */
    String appendUpload(String objectName, String filePath);

    /**
     * 断点上传
     */
    String breakpointUpload(String objectName, String filePath);

    /**
     * 分片上传，上传后合并
     * 最小分片 102400
     */
    String multiPartUpload(String objectName, String filePath);

    /**
     * 进度条上传
     */
    String processUpload(String objectName, String filePath);

    /**
     * post 请求，选择文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    String uploadFileAvatar(MultipartFile file) throws Exception;

    /**
     * 上传成功后回调
     *
     * @param objectName
     * @param filePath
     * @return
     * @throws Exception
     */
    String uploadCallBack(String objectName, String filePath) throws Exception;

    /**
     * 删除文件
     *
     * @param relativePath
     * @return
     */
    boolean delete(String relativePath) throws Exception;

    /**
     * bucket 权限管理 start
     *
     * @param bucketName
     * @param Access
     * @throws Exception
     */
    void setBucketAcl(String bucketName, String Access) throws Exception;

    String getBucketAcl(String bucketName) throws Exception;
    //bucket 权限管理 end

    /**
     * object 权限管理 start
     *
     * @param bucketName
     * @param objectName
     * @param Access
     * @throws Exception
     */
    void setObjectAcl(String bucketName, String objectName, String Access) throws Exception;

    String getObjectAcl(String bucketName, String objectName) throws Exception;
    // object 权限管理 end

}
