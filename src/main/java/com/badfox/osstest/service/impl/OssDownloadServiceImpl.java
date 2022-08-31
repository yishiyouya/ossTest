package com.badfox.osstest.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.*;
import com.badfox.osstest.configs.MyOssConfig;
import com.badfox.osstest.service.OssDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author 15210
 */
@Slf4j
@Service
public class OssDownloadServiceImpl implements OssDownloadService, ProgressListener {

    /**
     * oss 默认路径分割符
     */
    private final String DEFAULT_OSS_SEP = "/";
    @Autowired
    public OSS ossClient;
    @Autowired
    private MyOssConfig myOssConfig;
    @Autowired
    private WebApplicationContext context;
    /**
     * oss配置信息
     */
    private String endpoint = "";
    private String bucketName = "";

    /**
     * 进度条下载参数 start
     */
    private long bytesRead = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    // 进度条下载参数 end


    @PostConstruct
    public void initParas() {
        endpoint = this.myOssConfig.getALIYUN_OSS_ENDPOINT();
        bucketName = this.myOssConfig.getALIYUN_OSS_BUCKETNAME();
    }

    @Override
    public String downloadFile(String objectName, String pathName) throws Exception {
        String result = "";
        //检查文件目录是否存在
        OSS ossClient = (OSS) context.getBean("ossClient");

        pathName = generateDownFileName(objectName, pathName);
        checkPathExist(pathName);
        pathName = generateDownFileName(objectName, pathName);
        try {
            // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
            // 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
            ObjectMetadata objectMetadata = ossClient.getObject(new GetObjectRequest(bucketName, objectName),
                    new File(pathName));

            result = objectMetadata.getETag();
            log.info(result);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    @Override
    public void bufferDownloadFile(String objectName, String pathName) throws Exception {
        //检查文件目录是否存在
        OSS ossClient = (OSS) context.getBean("ossClient");

        pathName = generateDownFileName(objectName, pathName);
        checkPathExist(pathName);

        OSSObject ossObject = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            ossObject = ossClient.getObject(bucketName, objectName);

            // 读取文件内容。
            log.info("Object content:");
            reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pathName))));
            while (true) {
                String line = reader.readLine();
                writer.write(line);
                writer.newLine();
                if (line == null) {
                    break;
                }
            }
            writer.flush();

            log.info("bufferDownloadFile finished.");
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (Throwable ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } finally {
            // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            if (ossObject != null) {
                ossObject.close();
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public void rangeDownloadFile(String objectName, String pathName) throws Exception {
        //检查文件目录是否存在
        OSS ossClient = (OSS) context.getBean("ossClient");

        pathName = generateDownFileName(objectName, pathName);
        checkPathExist(pathName);

        InputStream in = null;
        OutputStream out = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
            // 对于大小为1000 Bytes的文件，正常的字节范围为0~999。
            // 获取0~999字节范围内的数据，包括0和999，共1000个字节的数据。如果指定的范围无效（比如开始或结束位置的指定值为负数，或指定值大于文件大小），则下载整个文件。
            getObjectRequest.setRange(0, 999);

            // 范围下载。
            OSSObject ossObject = ossClient.getObject(getObjectRequest);

            // 读取数据。
            byte[] buf = new byte[1024];
            in = ossObject.getObjectContent();

            // 写入本地文件
            out = new FileOutputStream(new File(pathName));
            byteArrayOutputStream = new ByteArrayOutputStream();
            int len;
            byte[] bytes = new byte[1024];
            while ((len = in.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }

            out.write(byteArrayOutputStream.toByteArray());
            out.flush();
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }
    }

    @Override
    public String breakPointDownloadFile(String objectName, String pathName) throws Exception {
        String result = "";
        //检查文件目录是否存在
        OSS ossClient = (OSS) context.getBean("ossClient");

        pathName = generateDownFileName(objectName, pathName);
        checkPathExist(pathName);

        ObjectMetadata objectMetadata = null;

        try {
            // 请求10个任务并发下载。
            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName, objectName);
            // 指定Object下载到本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
            downloadFileRequest.setDownloadFile(pathName);
            // 设置分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
            downloadFileRequest.setPartSize(1024 * 1024);
            // 设置分片下载的并发数，默认值为1。
            downloadFileRequest.setTaskNum(10);
            // 开启断点续传下载，默认关闭。
            downloadFileRequest.setEnableCheckpoint(true);
            // 设置断点记录文件的完整路径，例如D:\\localpath\\examplefile.txt.dcp。
            // 只有当Object下载中断产生了断点记录文件后，如果需要继续下载该Object，才需要设置对应的断点记录文件。下载完成后，该文件会被删除。
            //downloadFileRequest.setCheckpointFile("D:\\localpath\\examplefile.txt.dcp");

            // 下载文件。
            DownloadFileResult downloadRes = ossClient.downloadFile(downloadFileRequest);
            // 下载成功时，会返回文件元信息。
            if (null != downloadRes) {
                objectMetadata = downloadRes.getObjectMetadata();
                result = objectMetadata.getETag();
            }
            log.info(result);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (Throwable ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    @Override
    public String conditionDownloadFile(String objectName, String pathName) throws Exception {
        String result = "";
        //检查文件目录是否存在
        OSS ossClient = (OSS) context.getBean("ossClient");

        pathName = generateDownFileName(objectName, pathName);
        checkPathExist(pathName);

        ObjectMetadata objectMetadata = null;
        try {
            GetObjectRequest request = new GetObjectRequest(bucketName, objectName);
            // 设置限定条件。
            request.setModifiedSinceConstraint(new Date());

            // 下载OSS文件到本地文件。
            objectMetadata = ossClient.getObject(request, new File(pathName));

            result = objectMetadata.getETag();
            log.info(result);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    @Override
    public String processDwnloadFile(String objectName, String pathName) throws Exception {
        String result = "";
        //检查文件目录是否存在
        OSS ossClient = (OSS) context.getBean("ossClient");

        pathName = generateDownFileName(objectName, pathName);
        checkPathExist(pathName);

        ObjectMetadata objectMetadata = null;

        try {
            // 下载文件的同时指定了进度条参数。
            objectMetadata = ossClient.getObject(new GetObjectRequest(bucketName, objectName).
                            withProgressListener(new OssDownloadServiceImpl()),
                    new File(pathName));
            result = objectMetadata.getETag();
            log.info(result);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return result;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                log.info("Start to download......");
                break;
            case RESPONSE_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                log.info(this.totalBytes + " bytes in total will be downloaded to a local file");
                break;
            case RESPONSE_BYTE_TRANSFER_EVENT:
                this.bytesRead += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int) (this.bytesRead * 100.0 / this.totalBytes);
                    log.info(bytes + " bytes have been read at this time, download progress: " +
                            percent + "%(" + this.bytesRead + "/" + this.totalBytes + ")");
                } else {
                    log.info(bytes + " bytes have been read at this time, download ratio: unknown" +
                            "(" + this.bytesRead + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                log.info("Succeed to download, " + this.bytesRead + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                log.error("Failed to download, " + this.bytesRead + " bytes have been transferred");
                break;
            default:
                break;
        }
    }

    private boolean isSucceed() {
        return succeed;
    }


    /**
     * 检查下载文件路径是否存在
     *
     * @param pathName
     */
    private void checkPathExist(String pathName) {
        String filePath = pathName;
        int pathLastPathIdx = pathName.lastIndexOf(File.separator);
        if (pathLastPathIdx > 0) {
            filePath = pathName.substring(0, pathLastPathIdx);
        }
        log.info(filePath);
        File pathFile = new File(filePath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
    }

    /**
     * 获取 默认 pathName
     * D:\Download
     *
     * @param pathName
     * @return
     */
    private String getDefaultPath(String pathName) {
        if (!StringUtils.hasText(pathName)) {
            pathName = "D:\\Download";
        }
        return pathName;
    }

    /**
     * 根据 objectName 生成下载文件路径
     * objectName 路径追加到 filePath末尾
     * 例：
     * objectName: abc/1.txt
     * 下载后
     * D:\Download\abc\1.txt
     *
     * @param objectName
     * @param pathName
     * @return
     */
    private String generateDownFileName(String objectName, String pathName) throws Exception {

        pathName = getDefaultPath(pathName);

        String fileName = objectName;
        if (objectName.indexOf(DEFAULT_OSS_SEP) > -1) {
            fileName = objectName
                    .substring(objectName.indexOf(DEFAULT_OSS_SEP) + 1)
                    .replace(".", "_" + UUID.randomUUID() + ".");
            String tmpObjectName = objectName;
            if (!DEFAULT_OSS_SEP.equals(File.separator)) {
                tmpObjectName = objectName.substring(0, objectName.indexOf(DEFAULT_OSS_SEP))
                        .replace(DEFAULT_OSS_SEP, File.separator);
            }
            pathName = pathName.concat(File.separator).concat(tmpObjectName);
        }
        fileName = pathName.concat(File.separator).concat(fileName);
        return fileName;
    }

}
