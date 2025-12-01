package com.quick.boot.modules.common.files;

import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;

public interface FilesStorageService {

    /**
     * 创建bucket
     * @param bucketName bucket名称
     */
    void createBucket(String bucketName);

    /**
     * 上传文件
     * @param bucketName 存储桶名称
     * @param fileName 文件名称
     * @param stream 文件流
     * @param contextType 文件类型
     * @throws Exception
     */
    void putObject(String bucketName, String fileName, InputStream stream, String contextType)
            throws Exception;


    /**
     * 获取文件
     * @param bucketName bucket名称
     * @param fileName 文件名称
     * @return
     */
    S3Object getObject(String bucketName, String dir, String fileName);
}
