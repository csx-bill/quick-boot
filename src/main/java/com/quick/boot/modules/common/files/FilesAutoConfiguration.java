package com.quick.boot.modules.common.files;

import com.quick.boot.modules.common.files.local.LocalFilesStorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储自动配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(FilesProperties.class)
@ConditionalOnClass(FilesStorageService.class)
public class FilesAutoConfiguration {


    /**
     * 本地文件存储服务
     */
    @Bean
    @ConditionalOnMissingBean(FilesStorageService.class)
    @ConditionalOnProperty(name = "files.local.enable", havingValue = "true", matchIfMissing = true)
    public FilesStorageService localFileStorageService(FilesProperties properties) {
        return new LocalFilesStorageServiceImpl(properties);
    }

}
