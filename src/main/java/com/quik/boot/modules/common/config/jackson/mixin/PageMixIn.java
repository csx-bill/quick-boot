package com.quik.boot.modules.common.config.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Page类序列化混入配置
 * <p>
 * 重命名Mybatis Plus分页对象(Page)的records字段为"items"，
 * 使JSON输出更符合前端语义化要求
 */
public abstract class PageMixIn<T> {

    /**
     * 字段别名映射
     * <p>
     * 使用@JsonProperty注解将getRecords()方法对应的JSON字段
     * 名称从默认的"records"改为"items"
     */
    @JsonProperty("items")
    abstract List<T> getRecords();
}