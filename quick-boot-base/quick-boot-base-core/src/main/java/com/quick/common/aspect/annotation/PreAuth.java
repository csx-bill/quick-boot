package com.quick.common.aspect.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuth {

    String value() default "";

    /**
     * 替换父类@PreAuth注解中value的占位符{}
     *
     * @return 占位符
     */
    String replace() default "";
}
