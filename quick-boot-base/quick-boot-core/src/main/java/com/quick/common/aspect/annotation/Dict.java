package com.quick.common.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据字典注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    /**
     * 字典code
     *
     * @return
     */
    String dictCode();

    /**
     * 字典Text
     *
     * @return
     */
    String dictText() default "";
}
