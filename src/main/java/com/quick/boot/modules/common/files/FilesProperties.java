package com.quick.boot.modules.common.files;

import com.quick.boot.modules.common.files.local.LocalFilesProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 文件 配置信息
 * bucket 设置公共读权限
 */
@Data
@ConfigurationProperties(prefix = "files")
public class FilesProperties {
    /**
     * 默认的存储桶名称
     */
    private String bucketName = "local";

    /**
     * 本地文件配置信息
     */
    @NestedConfigurationProperty
    private LocalFilesProperties local;

}
