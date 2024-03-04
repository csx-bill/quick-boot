package com.quick.flow.engine.annotation;

import com.quick.flow.engine.config.FlowEngineConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * auto scan spring bean and mybatis mapper path
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({FlowEngineConfig.class})
public @interface EnableFlowEngine {

}
