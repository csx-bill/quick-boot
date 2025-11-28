package com.quick.boot.modules.system.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.quick.boot.modules.common.vo.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
@Tag(description = "file" , name = "文件管理" )
public class SysFileController {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("/yyyy/MM/dd/");

    @Value("${server.upload.path}")
    private String uploadPath;

    /**
     * 上传
     * @return R
     */
    @PostMapping("/upload")
    @Operation(summary = "上传" , description = "上传" )
    public R<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String name = LocalDate.now().format(DTF) + IdWorker.getIdStr() + "." + FilenameUtils.getExtension(file.getOriginalFilename());


        // 创建上传目录
        Path filePath = Paths.get(uploadPath, name);
        Path subDirs = filePath.getParent();
        if (!Files.exists(subDirs)) {
            Files.createDirectories(subDirs);
        }
        file.transferTo(filePath);

        Map<String, String> result = new HashMap<>();
        // /files前缀与StaticResourceConfig中addResourceHandler配置保持一致
        result.put("value", "/files" + name);
        return R.ok(result);
    }
}
