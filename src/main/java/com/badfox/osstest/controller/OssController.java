package com.badfox.osstest.controller;

import com.badfox.osstest.service.OssDownloadService;
import com.badfox.osstest.service.OssUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author 15210
 */
@Slf4j
@RestController
public class OssController {

    @Autowired
    private OssUploadService ossUploadService;

    @Autowired
    private OssDownloadService ossDownloadService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/test")
    public String test() {
        log.info("========test========");
        return "test";
    }

    @RequestMapping("/upload")
    public String upload() throws Throwable {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如 badfox/testOSS.txt。
        String objectName = "badfox/testOSS.txt";
        // 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String fileName = "D:\\Download\\bonc\\OSS\\testOSS_Down1.txt";
        String result = ossUploadService.upload(objectName, fileName);
        return result;
    }

    @RequestMapping("/appendUpload")
    public String rspAppendUpload() throws Throwable {
        // 填写Object完整路径，完整路径中不能包含Bucket名称。
        String objectName = "appendupload/1.txt";
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String fileName = "D:\\Download\\bonc\\OSS\\appendupload";
        String result = ossUploadService.appendUpload(objectName, fileName);
        return result;
    }

    @RequestMapping("/processUpload")
    public String rspProcessUpload() throws Throwable {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如 badfox/testOSS.txt。
        String objectName = "processUpload/1.txt";
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String fileName = "D:\\Download\\bonc\\OSS\\processUpload\\1.txt";
        String result = ossUploadService.processUpload(objectName, fileName);
        return result;
    }

    @RequestMapping("/breakpointUpload")
    public String rspBreakpointUpload() throws Throwable {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如 badfox/testOSS.txt。
        String objectName = "breakpoint/1.txt";
        // 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String fileName = "D:\\Download\\bonc\\OSS\\breakpoint\\1.txt";
        String result = ossUploadService.breakpointUpload(objectName, fileName);
        return result;
    }

    @RequestMapping("/multiPartUpload")
    public String rspMultiPartUpload() throws Throwable {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如 badfox/testOSS.txt。
        String objectName = "multiPartUpload/1.txt";
        // 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String fileName = "D:\\Download\\bonc\\OSS\\multiPartUpload\\1.txt";
        String result = ossUploadService.multiPartUpload(objectName, fileName);
        return result;
    }

    /**
     * 上传头像，返回图片的url
     */
    @PostMapping("/uploadFileAvatar")
    public String uploadOssFile(MultipartFile file) throws Throwable {
        //获取上传文件 MultipartFile
        //返回图片在oss上的路径
        String url = ossUploadService.uploadFileAvatar(file);
        return url;
    }

    @RequestMapping("/uploadCallBack")
    public String rspUploadCallBack() throws Throwable {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如 badfox/testOSS.txt。
        String objectName = "badfox/testOSSBig.txt";
        // 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String fileName = "D:\\Download\\bonc\\OSS\\testOSS_Down_Big.txt";

        String result = ossUploadService.uploadCallBack(objectName, fileName);
        return result;
    }

    @RequestMapping("/downloadFile")
    public String rspDownloadFile(@RequestParam("objectName") String objectName,
                                  @RequestParam(value = "pathName", required = false) String pathName)
            throws Throwable {

        //// 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        //// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String result = ossDownloadService.downloadFile(objectName, pathName);
        return result;
    }

    @RequestMapping("/limitSpeedDownloadFile")
    public String rspLimitSpeedDownloadFile(@RequestParam("objectName") String objectName,
                                            @RequestParam(value = "pathName", required = false) String pathName,
                                            @RequestParam(value = "limitSpeed", required = false) Integer limitSpeed)
            throws Throwable {

        //// 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        //// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String result = ossDownloadService.limitSpeedDownloadFile(objectName, pathName, limitSpeed);
        return result;
    }

    @RequestMapping("/bufferDownloadFile")
    public String rspBufferDownloadFile(@RequestParam("objectName") String objectName,
                                        @RequestParam(value = "pathName", required = false) String pathName)
            throws Throwable {

        //// 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        //// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        ossDownloadService.bufferDownloadFile(objectName, pathName);
        return "bufferDownloadFile";
    }

    @RequestMapping("/rangeDownloadFile")
    public String rspRangeDownloadFile(@RequestParam("objectName") String objectName,
                                       @RequestParam(value = "pathName", required = false) String pathName)
            throws Throwable {

        //// 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        //// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        ossDownloadService.rangeDownloadFile(objectName, pathName);
        return "rangeDownloadFile";
    }


    @RequestMapping("/breakPointDownloadFile")
    public String rspBreakPointDownloadFile(@RequestParam("objectName") String objectName,
                                            @RequestParam(value = "pathName", required = false) String pathName)
            throws Throwable {

        //// 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        //// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String result = ossDownloadService.breakPointDownloadFile(objectName, pathName);
        return result;
    }

    @RequestMapping("/conditionDownloadFile")
    public String rspConditionDownloadFile(@RequestParam("objectName") String objectName,
                                           @RequestParam(value = "pathName", required = false) String pathName)
            throws Throwable {

        //// 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        //// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String result = ossDownloadService.conditionDownloadFile(objectName, pathName);
        return result;
    }


    @RequestMapping("/processDwnloadFile")
    public String rspProcessDwnloadFile(@RequestParam("objectName") String objectName,
                                        @RequestParam(value = "pathName", required = false) String pathName)
            throws Throwable {

        //// 填写本地文件的完整路径，例如D://Download//bonc//testOSS.txt
        //// 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String result = ossDownloadService.processDwnloadFile(objectName, pathName);
        return result;
    }

    @RequestMapping("/setBucketAcl")
    public String rspSetBucketAcl(@RequestParam("bucketName") String bucketName,
                                  @RequestParam("Access") String Access) {
        String result = "success";
        try {
            ossUploadService.setBucketAcl(bucketName, Access);
        } catch (Throwable e) {
            result = "fail";
            throw new RuntimeException(e);
        }
        return result;
    }

    @RequestMapping("/getBucketAcl")
    public String rspGetBucketAcl(@RequestParam("bucketName") String bucketName) {
        String bucketAcl = "";
        try {
            bucketAcl = ossUploadService.getBucketAcl(bucketName);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return bucketAcl;
    }

    /**
     * @param bucketName
     * @param objectName
     * @param Access     Default("default"),
     *                   Private("private"),
     *                   PublicRead("public-read"),
     *                   PublicReadWrite("public-read-write")
     * @return
     */
    @RequestMapping("/setObjectAcl")
    public String rspSetObjectAcl(@RequestParam("bucketName") String bucketName,
                                  @RequestParam("objectName") String objectName,
                                  @RequestParam("Access") String Access) {
        String result = "success";
        try {
            ossUploadService.setObjectAcl(bucketName, objectName, Access);
        } catch (Throwable e) {
            result = "fail";
            throw new RuntimeException(e);
        }
        return result;
    }

    @RequestMapping("/getObjectAcl")
    public String rspGetObjectAcl(@RequestParam("bucketName") String bucketName,
                                  @RequestParam("objectName") String objectName) {
        String objectAcl = "";
        try {
            objectAcl = ossUploadService.getObjectAcl(bucketName, objectName);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return objectAcl;
    }

}
