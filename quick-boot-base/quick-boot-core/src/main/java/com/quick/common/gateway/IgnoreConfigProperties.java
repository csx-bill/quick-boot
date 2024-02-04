package com.quick.common.gateway;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 无需认证的地址
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "ignore")
public class IgnoreConfigProperties {

    /**
     * 无需认证的地址
     * 从 nacos 读取 忽略url
     */
    private List<String> url = new ArrayList<>();

}
