package com.quik.boot.modules.common.config.jackson;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.quik.boot.modules.common.config.jackson.mixin.PageMixIn;
import com.quik.boot.modules.common.constant.DatePatternConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jackson.autoconfigure.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;
/**
 * 自定义Jackson序列化配置
 */

@Configuration
public class JacksonConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // 通过混入(PageMixIn)覆盖Mybatis Plus的Page类序列化行为，
            builder.mixIn(Page.class, PageMixIn.class);

            builder.locale(Locale.CHINA);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(DatePatternConstants.NORM_DATETIME_PATTERN);
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.modules(new JavaTimeModule());
        };
    }

}
