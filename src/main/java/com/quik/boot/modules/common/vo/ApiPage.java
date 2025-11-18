package com.quik.boot.modules.common.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页参数")
public class ApiPage<T>extends Page<T> {
    public ApiPage() {
        super(1, 10); // 默认值
    }

    @JsonCreator
    public ApiPage(@JsonProperty("page") Long current,
                   @JsonProperty("perPage") Long size) {
        super(current != null ? current : 1, size != null ? size : 10);
    }

    /**
     * 序列化: current -> page
     */
    @Schema(description = "当前页码", example = "1")
    @JsonProperty("page")
    @Override
    public long getCurrent() {
        return super.getCurrent();
    }

    /**
     * 序列化: size -> perPage
     */
    @Schema(description = "每页数量", example = "10")
    @JsonProperty("perPage")
    @Override
    public long getSize() {
        return super.getSize();
    }

    /**
     * 序列化: records -> items
     */
    @Schema(description = "数据列表")
    @JsonProperty("items")
    @Override
    public List<T> getRecords() {
        return super.getRecords();
    }

    /**
     * 反序列化: page -> current
     */
    @JsonProperty("page")
    public void setPage(Long page) {
        if (page != null && page > 0) {
            super.setCurrent(page);
        }
    }

    /**
     * 反序列化: perPage -> size
     */
    @JsonProperty("perPage")
    public void setPerPage(Long perPage) {
        if (perPage != null && perPage > 0) {
            super.setSize(perPage);
        }
    }

    /**
     * 为了支持 @ParameterObject 参数绑定，需要重写 setCurrent 和 setSize
     */
    @Override
    public Page<T> setCurrent(long current) {
        if (current > 0) {
            super.setCurrent(current);
        }
        return this;
    }

    @Override
    public Page<T> setSize(long size) {
        if (size > 0) {
            super.setSize(size);
        }
        return this;
    }
}
