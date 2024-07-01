package com.quick.online.config;

import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DynamicDataSourceAutoConfiguration {
    private final DynamicDataSourceProperties properties;
    /**
     * 获取动态数据源提供者
     * @param defaultDataSourceCreator 默认数据源创建器
     * @return 动态数据源提供者
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator) {
        return new JdbcDynamicDataSourceProvider(defaultDataSourceCreator, properties.getDatasource().get("master").getDriverClassName(),properties.getDatasource().get("master").getUrl(), properties.getDatasource().get("master").getUsername(), properties.getDatasource().get("master").getPassword());
    }
}
