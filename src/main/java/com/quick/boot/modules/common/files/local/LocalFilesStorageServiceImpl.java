package com.quick.boot.modules.common.files.local;

import com.amazonaws.services.s3.model.S3Object;
import com.quick.boot.modules.common.files.FilesProperties;
import com.quick.boot.modules.common.files.FilesStorageService;
import com.quick.boot.modules.common.project.ProjectContextHolder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@AllArgsConstructor
public class LocalFilesStorageServiceImpl implements FilesStorageService {

    private final FilesProperties properties;

    @SneakyThrows
    @Override
    public void createBucket(String bucketName) {
        Files.createDirectories(Paths.get(properties.getLocal().getBasePath(), bucketName));
    }

    @Override
    public void putObject(String bucketName, String fileName, InputStream stream, String contextType) throws Exception {
        // 检查存储桶是否存在，如果不存在则创建
        if (!Files.isDirectory(Paths.get(properties.getLocal().getBasePath(), bucketName))) {
            createBucket(bucketName);
        }
        // 按项目隔离
        Long projectId = ProjectContextHolder.getProjectId();
        String dir = properties.getLocal().getBasePath() + File.separator + bucketName;
        // 写入文件
        Path filePath = Paths.get(dir, projectId.toString(), fileName);
        // 确保项目目录存在
        Files.createDirectories(filePath.getParent());

        Files.copy(stream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @SneakyThrows
    @Override
    public S3Object getObject(String bucketName,String dir, String fileName) {
        S3Object s3Object = new S3Object();
        Path filePath = Paths.get(properties.getLocal().getBasePath(), bucketName, dir, fileName);
        InputStream inputStream = new BufferedInputStream(Files.newInputStream(filePath));
        s3Object.setObjectContent(inputStream);
        return s3Object;
    }

}
