package com.quick.boot.modules.system.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.boot.modules.common.files.FilesProperties;
import com.quick.boot.modules.common.files.FilesStorageService;
import com.quick.boot.modules.common.project.ProjectContextHolder;
import com.quick.boot.modules.common.vo.R;
import com.quick.boot.modules.system.entity.SysFiles;
import com.quick.boot.modules.system.mapper.SysFilesMapper;
import com.quick.boot.modules.system.service.SysFilesService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class SysFilesServiceImpl extends ServiceImpl<SysFilesMapper, SysFiles> implements SysFilesService {

    private final FilesStorageService filesStorageService;

    private final FilesProperties properties;

    @SneakyThrows
    @Override
    public R uploadFile(MultipartFile file, String type) {
        // 生成简单UUID（无横线）
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 安全提取文件扩展名
        String extName = Optional.ofNullable(file.getOriginalFilename())
                .filter(name -> !name.trim().isEmpty())
                .filter(name -> name.contains("."))
                .map(name -> {
                    int lastDotIndex = name.lastIndexOf(".");
                    // 确保不是隐藏文件且有实际扩展名
                    return (lastDotIndex > 0 && lastDotIndex < name.length() - 1)
                            ? name.substring(lastDotIndex + 1)
                            : "";
                })
                .orElse("");

        // 只有扩展名不为空时才添加点号
        String fileName = extName.isEmpty() ? uuid : uuid + "." + extName;

        Map<String, String> resultMap = new HashMap<>(4);
        resultMap.put("bucketName", properties.getBucketName());
        resultMap.put("fileName", fileName);

        resultMap.put("url", String.format("/api/files/oss/file?fileName=%s", fileName));

        try (InputStream inputStream = file.getInputStream()) {
            filesStorageService.putObject(properties.getBucketName(), fileName, inputStream, file.getContentType());
            fileLog(file,fileName, type);
        }
        catch (Exception e) {
            log.error("上传失败", e);
            return R.failed(e.getLocalizedMessage());
        }
        return R.ok(resultMap);
    }

    @Override
    public void getFile(String fileName, HttpServletResponse response) {
        SysFiles sysFile = baseMapper.selectOne(Wrappers.<SysFiles>lambdaQuery().eq(SysFiles::getFileName, fileName));
        try (S3Object s3Object = filesStorageService.getObject(sysFile.getBucketName(), sysFile.getDir(), fileName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            InputStream inputStream = s3Object.getObjectContent();
            OutputStream outputStream = response.getOutputStream();
            inputStream.transferTo(outputStream);
        }
        catch (Exception e) {
            log.error("文件读取异常: {}", e.getLocalizedMessage());
        }
    }

    /**
     * 文件管理数据记录
     * @param file 上传的文件
     * @param fileName 文件名
     * @param type 文件类型
     */
    private void fileLog(MultipartFile file, String fileName, String type) {
        SysFiles sysFile = new SysFiles();
        sysFile.setFileName(fileName);

        // 对原始文件名进行编码转换
        String originalFilename = new String(
                Objects.requireNonNull(file.getOriginalFilename()).getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);
        sysFile.setDir(ProjectContextHolder.getProjectId().toString());
        sysFile.setOriginal(originalFilename);
        sysFile.setFileSize(file.getSize());
        sysFile.setBucketName(properties.getBucketName());
        sysFile.setType(type);
        sysFile.setProjectId(ProjectContextHolder.getProjectId());
        this.save(sysFile);
    }
}
