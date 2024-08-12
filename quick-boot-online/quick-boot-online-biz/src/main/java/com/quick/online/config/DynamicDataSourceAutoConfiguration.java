package com.quick.online.config;

import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsJakartaHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsJakartaSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DynamicDataSourceAutoConfiguration {
    private final DynamicDataSourceProperties properties;

    /**
     * 获取动态数据源提供者
     * 
     * @param defaultDataSourceCreator 默认数据源创建器
     * @return 动态数据源提供者
     */
    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator) {
        // 读取yaml配置文件中的spring:datasource:dynamic:primary值
        String primary = properties.getPrimary();
        if (StrUtil.isBlank(primary)) {
            primary = "master";
        }

        log.info("dynamicDataSourceProvider primary: {}", primary);

        return new JdbcDynamicDataSourceProvider(defaultDataSourceCreator,
                properties.getDatasource().get(primary).getDriverClassName(),
                properties.getDatasource().get(primary).getUrl(), properties.getDatasource().get(primary).getUsername(),
                properties.getDatasource().get(primary).getPassword());
    }

    /**
     * 获取数据源处理器
     * 
     * @return 数据源处理器
     */
    @Bean
    public DsProcessor dsProcessor(BeanFactory beanFactory) {
        DsProcessor lastParamDsProcessor = new LastParamDsProcessor();
        DsProcessor headerProcessor = new DsJakartaHeaderProcessor();
        DsProcessor sessionProcessor = new DsJakartaSessionProcessor();
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
        lastParamDsProcessor.setNextProcessor(headerProcessor);
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(spelExpressionProcessor);
        return lastParamDsProcessor;
    }
}
