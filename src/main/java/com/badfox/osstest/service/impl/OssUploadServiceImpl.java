package com.badfox.osstest.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.*;
import com.badfox.osstest.configs.MyOssConfig;
import com.badfox.osstest.service.OssUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 15210
 */
@Service
@Slf4j
public class OssUploadServiceImpl implements OssUploadService, ProgressListener {

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
     * 进度条上传参数 start
     */
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    // 进度条上传参数 end


    @PostConstruct
    public void initParas() {
        endpoint = this.myOssConfig.getALIYUN_OSS_ENDPOINT();
        bucketName = this.myOssConfig.getALIYUN_OSS_BUCKETNAME();
    }

    @Override
    public String upload(String objectName, String filePath) {
        // 创建OSSClient实例。
        OSS ossClient = (OSS) context.getBean("ossClient");
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                    objectName,
                    new File(filePath));
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件。
            ossClient.putObject(putObjectRequest);
            ossClient.shutdown();

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "your link";
    }

    /**
     * filePath 文件夹下所有文件名
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public List<File> getAllFileByPath(String filePath) throws Exception {
        File pathFile = new File(filePath);
        List<File> files = new ArrayList<>();
        files = Arrays.asList(pathFile.listFiles());
        return files;
    }

    @Override
    public String appendUpload(String objectName, String filePath) {
        String result = "";

        // 创建OSSClient实例。
        OSS ossClient = (OSS) context.getBean("ossClient");
        try {
            //filePath 文件夹下所有文件
            List<File> files = getAllFileByPath(filePath);
            if (null == files || files.size() < 1) {
                throw new Exception("无文件");
            }


            ObjectMetadata meta = new ObjectMetadata();
            // 指定上传的内容类型。
            meta.setContentType("text/plain");
            // 指定该Object的网页缓存行为。
            //meta.setCacheControl("no-cache");
            // 指定该Object被下载时的名称。
            //meta.setContentDisposition("attachment;filename=oss_download.txt");
            // 指定该Object的内容编码格式。
            //meta.setContentEncoding(OSSConstants.DEFAULT_CHARSET_NAME);
            // 该请求头用于检查消息内容是否与发送时一致。
            //meta.setContentMD5("ohhnqLBJFiKkPSBO1eNaUA==");
            // 指定过期时间。
            //try {
            //    meta.setExpirationTime(DateUtil.parseRfc822Date("Wed, 08 Jul 2022 16:57:01 GMT"));
            //} catch (ParseException e) {
            //    e.printStackTrace();
            //}
            // 指定服务器端加密方式。此处指定为OSS完全托管密钥进行加密（SSE-OSS）。
            //meta.setServerSideEncryption(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
            // 指定Object的访问权限。此处指定为私有访问权限。
            //meta.setObjectAcl(CannedAccessControlList.Private);
            // 指定Object的存储类型。
            //meta.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard);
            // 创建AppendObject时可以添加x-oss-meta-*，继续追加时不可以携带此参数。如果配置以x-oss-meta-*为前缀的参数，则该参数视为元数据。
            //meta.setHeader("x-oss-meta-author", "Alice");

            // 通过AppendObjectRequest设置多个参数。
            AppendObjectRequest appendObjectRequest =
                    new AppendObjectRequest(bucketName,
                            objectName,
                            new FileInputStream(files.get(0)), meta);

            // 通过AppendObjectRequest设置单个参数。
            // 设置Bucket名称。
            //appendObjectRequest.setBucketName(bucketName);
            // 设置Object名称。
            //appendObjectRequest.setKey(objectName);
            // 设置待追加的内容。可选类型包括InputStream类型和File类型。此处为InputStream类型。
            //appendObjectRequest.setInputStream(new ByteArrayInputStream(content1.getBytes()));
            // 设置待追加的内容。可选类型包括InputStream类型和File类型。此处为File类型。
            //appendObjectRequest.setFile(new File("D:\\localpath\\examplefile.txt"));
            // 指定文件的元信息，第一次追加时有效。
            //appendObjectRequest.setMetadata(meta);

            // 第一次追加。
            // 设置文件的追加位置。
            appendObjectRequest.setPosition(0L);
            AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
            // 文件的64位CRC值。此值根据ECMA-182标准计算得出。
            System.out.println(appendObjectResult.getObjectCRC());

            //// 第二次追加。
            //// nextPosition表示下一次请求中应当提供的Position，即文件当前的长度。
            //appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
            //appendObjectRequest.setInputStream(new FileInputStream(new File(files.get(1))));
            //appendObjectResult = ossClient.appendObject(appendObjectRequest);
            //
            //// 第三次追加。
            //appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
            //appendObjectRequest.setInputStream(new FileInputStream(new File(files.get(2))));
            //appendObjectResult = ossClient.appendObject(appendObjectRequest);

            for (int i = 1; i < files.size(); i++) {
                appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
                appendObjectRequest.setInputStream(new FileInputStream(files.get(i)));
                appendObjectResult = ossClient.appendObject(appendObjectRequest);
            }

            result = appendObjectResult.toString();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (Exception e) {
            System.out.println("Error Message:" + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    @Override
    public String processUpload(String objectName, String filePath) {
        // 创建OSSClient实例。
        String result = "";
        OSS ossClient = (OSS) context.getBean("ossClient");
        try {
            // 上传文件的同时指定进度条参数。此处PutObjectProgressListenerDemo为调用类的类名，请在实际使用时替换为相应的类名。
            PutObjectResult putObjectResult = ossClient.putObject(
                    new PutObjectRequest(
                            bucketName,
                            objectName,
                            new File(filePath)).withProgressListener(new OssUploadServiceImpl()));
            result = putObjectResult.getRequestId();
            /**
             * 下载文件的同时指定进度条参数。此处GetObjectProgressListenerDemo为调用类的类名，请在实际使用时替换为相应的类名。
             * ossClient.getObject(new GetObjectRequest(bucketName,objectName).
             * <GetObjectRequest>withProgressListener(new GetObjectProgressListenerDemo()));
             */
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    @Override
    public String breakpointUpload(String objectName, String filePath) {
        String result = "";
        try {
            OSS ossClient = (OSS) context.getBean("ossClient");
            ObjectMetadata meta = new ObjectMetadata();
            // 指定上传的内容类型。
            meta.setContentType("text/plain");

            // 文件上传时设置访问权限ACL。
            // meta.setObjectAcl(CannedAccessControlList.Private);

            // 通过UploadFileRequest设置多个参数。
            // 依次填写Bucket名称（例如examplebucket）以及Object完整路径（例如exampledir/exampleobject.txt），Object完整路径中不能包含Bucket名称。
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, objectName);

            // 通过UploadFileRequest设置单个参数。
            // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
            uploadFileRequest.setUploadFile(filePath);
            // 指定上传并发线程数，默认值为1。
            uploadFileRequest.setTaskNum(5);
            // 指定上传的分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
            uploadFileRequest.setPartSize(1024 * 1024);
            // 开启断点续传，默认关闭。
            uploadFileRequest.setEnableCheckpoint(true);
            // 记录本地分片上传结果的文件。上传过程中的进度信息会保存在该文件中，如果某一分片上传失败，再次上传时会根据文件中记录的点继续上传。上传完成后，该文件会被删除。
            // 如果未设置该值，默认与待上传的本地文件同路径，名称为${uploadFile}.ucp。
            uploadFileRequest.setCheckpointFile("yourCheckpointFile");
            // 文件的元数据。
            uploadFileRequest.setObjectMetadata(meta);
            // 设置上传回调，参数为Callback类型。
            //uploadFileRequest.setCallback("yourCallbackEvent");

            // 断点续传上传。
            UploadFileResult uploadFileResult = ossClient.uploadFile(uploadFileRequest);

            result = uploadFileResult.getMultipartUploadResult().toString();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Throwable ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            // 关闭OSSClient。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    @Override
    public String multiPartUpload(String objectName, String filePath) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String result = "";
        // 获取 OSSClient实例。
        OSS ossClient = (OSS) context.getBean("ossClient");
        try {
            // 创建InitiateMultipartUploadRequest对象。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);

            // 如果需要在初始化分片时设置请求头，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // 指定该Object的网页缓存行为。
            // metadata.setCacheControl("no-cache");
            // 指定该Object被下载时的名称。
            // metadata.setContentDisposition("attachment;filename=oss_MultipartUpload.txt");
            // 指定该Object的内容编码格式。
            // metadata.setContentEncoding(OSSConstants.DEFAULT_CHARSET_NAME);
            // 指定初始化分片上传时是否覆盖同名Object。此处设置为true，表示禁止覆盖同名Object。
            // metadata.setHeader("x-oss-forbid-overwrite", "true");
            // 指定上传该Object的每个part时使用的服务器端加密方式。
            // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION, ObjectMetadata.KMS_SERVER_SIDE_ENCRYPTION);
            // 指定Object的加密算法。如果未指定此选项，表明Object使用AES256加密算法。
            // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_DATA_ENCRYPTION, ObjectMetadata.KMS_SERVER_SIDE_ENCRYPTION);
            // 指定KMS托管的用户主密钥。
            // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION_KEY_ID, "9468da86-3509-4f8d-a61e-6eab1eac****");
            // 指定Object的存储类型。
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard);
            // 指定Object的对象标签，可同时设置多个标签。
            // metadata.setHeader(OSSHeaders.OSS_TAGGING, "a:1");
            // request.setObjectMetadata(metadata);

            // 初始化分片。
            InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
            // 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关的操作，例如取消分片上传、查询分片上传等。
            String uploadId = upresult.getUploadId();

            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
            List<PartETag> partETags = new ArrayList<PartETag>();
            // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
            final long partSize = 1024 * 1024L;

            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
            final File sampleFile = new File(filePath);
            long fileLength = sampleFile.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }
            // 遍历分片上传。
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                InputStream instream = new FileInputStream(sampleFile);
                // 跳过已经上传的分片。
                instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(instream);
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
                uploadPartRequest.setPartNumber(i + 1);
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
                partETags.add(uploadPartResult.getPartETag());
            }
            // 创建CompleteMultipartUploadRequest对象。
            // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);

            // 如果需要在完成分片上传的同时设置文件访问权限，请参考以下示例代码。
            // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.Private);
            // 指定是否列举当前UploadId已上传的所有Part。如果通过服务端List分片数据来合并完整文件时，以上CompleteMultipartUploadRequest中的partETags可为null。
            // Map<String, String> headers = new HashMap<String, String>();
            // 如果指定了x-oss-complete-all:yes，则OSS会列举当前UploadId已上传的所有Part，然后按照PartNumber的序号排序并执行CompleteMultipartUpload操作。
            // 如果指定了x-oss-complete-all:yes，则不允许继续指定body，否则报错。
            // headers.put("x-oss-complete-all","yes");
            // completeMultipartUploadRequest.setHeaders(headers);

            // 完成分片上传。
            CompleteMultipartUploadResult completeMultipartUploadResult =
                    ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            System.out.println(completeMultipartUploadResult.getETag());

            stopWatch.stop();

            result = completeMultipartUploadResult.getETag() + stopWatch.getLastTaskTimeMillis();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            result = "OSSException multiPartUpload failed";
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            result = "ClientException multiPartUpload failed";
        } catch (FileNotFoundException e) {
            result = "FileNotFoundException multiPartUpload failed";
            throw new RuntimeException(e);
        } catch (IOException e) {
            result = "IOException multiPartUpload failed";
            throw new RuntimeException(e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    @Override
    public String uploadFileAvatar(MultipartFile file) throws Exception {
        String url = null;

        OSS ossClient = (OSS) context.getBean("ossClient");
        //获取上传文件输入流
        InputStream inputStream = file.getInputStream();
        //获取文件名称
        String fileName = file.getOriginalFilename();

        //保证文件名唯一，去掉uuid中的'-'
        //String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //fileName = uuid + fileName;

        // 把文件按日期分类
        //LocalDate localDate = LocalDate.now();
        //String datePath = localDate.getYear()+"-"+localDate.getMonthValue()+"-"+localDate.getDayOfMonth();
        ////拼接
        //fileName = datePath + "/" + fileName;

        //调用oss方法上传到阿里云
        //第一个参数：Bucket名称
        //第二个参数：上传到oss文件路径和文件名称
        //第三个参数：上传文件输入流
        ossClient.putObject(bucketName, fileName, inputStream);

        //把上传后把文件url返回
        url = endpoint + "/" + fileName;
        url = url.replace("http://", "http://" + bucketName + ".");
        //关闭OSSClient
        ossClient.shutdown();

        return url;
    }

    @Override
    public String uploadCallBack(String objectName, String filePath) throws Exception {
        String result = "";

        /**
         * 需要公网地址，且返回 status 200
         *您的回调服务器地址，例如https://example.com:23450或者https://127.0.0.1:9090。
         */
        //String callbackUrl = "https://badfox.oss-cn-hangzhou.aliyuncs.com/mountain1.png";
        String callbackUrl = "http://192.168.1.149:9000/";

        // 创建OSSClient实例
        OSS ossClient = (OSS) context.getBean("ossClient");
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                    objectName,
                    new File(filePath));
            // 上传回调参数
            Callback callback = new Callback();
            callback.setCallbackUrl(callbackUrl);
            //（可选）设置回调请求消息头中Host的值，即您的服务器配置Host的值。
            // callback.setCallbackHost("yourCallbackHost");
            // 设置发起回调时请求body的值。
            callback.setCallbackBody("{\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}");
            // 设置发起回调请求的Content-Type。
            callback.setCalbackBodyType(Callback.CalbackBodyType.JSON);
            // 设置发起回调请求的自定义参数，由Key和Value组成，Key必须以x:开始。
            callback.addCallbackVar("x:var1", "value1");
            putObjectRequest.setCallback(callback);

            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);

            // 读取上传回调返回的消息内容。
            byte[] buffer = new byte[1024];
            putObjectResult.getResponse().getContent().read(buffer);
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            putObjectResult.getResponse().getContent().close();

            result = putObjectResult.toString();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Throwable ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    @Override
    public boolean delete(String relativePath) throws Exception {
        OSS ossClient = (OSS) context.getBean("ossClient");
        try {
            ossClient.deleteObject(bucketName, relativePath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            ossClient.shutdown();
        }
        return true;
    }

    @Override
    public String getBucketAcl(String bucketName) throws Exception {
        // OSS ossClient = new OSSClientBuilder().build(
        //        myOssConfig.getALIYUN_OSS_ENDPOINT(),
        //        myOssConfig.getALIYUN_OSS_ACCESSKEYID(),
        //        myOssConfig.getALIYUN_OSS_ACCESSKEYSECRET());

        System.out.println(ossClient.getConnectionPoolStats() + "==========start");
        OSS ossClient = (OSS) context.getBean("ossClient");
        System.out.println(ossClient.hashCode());
        AccessControlList bucketAcl = ossClient.getBucketAcl(bucketName);

        ossClient.shutdown();
        System.out.println(ossClient.getConnectionPoolStats() + "==========end");
        return bucketAcl.toString();
    }

    @Override
    public void setBucketAcl(String bucketName, String Access) throws Exception {
        //创建OSSClient实例。
        OSS ossClient = (OSS) context.getBean("ossClient");
        ossClient.setBucketAcl(bucketName, canAccessListByAccess(Access));

        ossClient.shutdown();
    }

    @Override
    public void setObjectAcl(String bucketName, String objectName, String Access) throws Exception {
        //创建OSSClient实例。
        OSS ossClient = (OSS) context.getBean("ossClient");
        ossClient.setObjectAcl(bucketName, objectName, canAccessListByAccess(Access));
        ossClient.shutdown();

    }

    @Override
    public String getObjectAcl(String bucketName, String objectName) throws Exception {
        //创建OSSClient实例。
        OSS ossClient = (OSS) context.getBean("ossClient");
        ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, objectName);

        ossClient.shutdown();

        return objectAcl.getPermission().toString();
    }

    public boolean isSucceed() {
        return succeed;
    }

    /**
     * 使用进度条需要您重写回调方法，以下仅为参考示例。
     */
    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                System.out.println("Start to upload......");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                    System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                } else {
                    System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;
            default:
                break;
        }
    }

    /**
     * 根据 Access 生成 CannedAccessControlList类型入参
     *
     * @param Access
     * @return
     * @throws Exception
     */
    private CannedAccessControlList canAccessListByAccess(String Access) throws Exception {
        CannedAccessControlList result = null;
        if (CannedAccessControlList.PublicRead.toString().equals(Access)) {
            result = CannedAccessControlList.PublicRead;
        } else if (CannedAccessControlList.PublicReadWrite.toString().equals(Access)) {
            result = CannedAccessControlList.PublicReadWrite;
        } else if (CannedAccessControlList.Private.toString().equals(Access)) {
            result = CannedAccessControlList.Private;
        } else if (CannedAccessControlList.Default.toString().equals(Access)) {
            result = CannedAccessControlList.Default;
        } else {
            throw new Exception("no this Access!");
        }
        return result;
    }


}
