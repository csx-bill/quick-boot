package com.quick.common.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户的配置
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "quick.tenant")
public class TenantConfigProperties {

    /**
     * 多租户的表集合
     */
    private List<String> tables = new ArrayList<>();

}
