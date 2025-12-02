package com.quick.boot.modules.common.config.jackson;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.quick.boot.modules.common.constant.DatePatternConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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
    public Jackson2ObjectMapperBuilder customizer() {
        return new Jackson2ObjectMapperBuilder()
                .locale(Locale.CHINA)
                .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                .simpleDateFormat(DatePatternConstants.NORM_DATETIME_PATTERN)
                .serializerByType(Long.class, ToStringSerializer.instance)
                .modules(new JavaTimeModule());
    }

}
