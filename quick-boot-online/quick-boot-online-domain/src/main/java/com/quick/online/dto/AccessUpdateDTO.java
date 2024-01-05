package com.quick.online.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "更新-表单管理")
public class AccessUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id;

    @Schema(description = "是否为 DEBUG: 0-否，1-是。")
    private int debug;

    @Schema(description = "数据库名/模式")
    private String schema;

    @Schema(description = "表名")
    private String name;

    @Schema(description = "表别名")
    private String alias;

    @Schema(description = "描述")
    private String detail;

    @Schema(description = "删除字段名")
    private String deletedKey;

    @Schema(description = "删除字段值")
    private String deletedValue;

    @Schema(description = "未删除字段值")
    private String notDeletedValue;

}
