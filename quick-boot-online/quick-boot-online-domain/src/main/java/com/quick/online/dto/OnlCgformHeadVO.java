package com.quick.online.dto;

import com.quick.online.entity.OnlCgformField;
import com.quick.online.entity.OnlCgformHead;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class OnlCgformHeadVO extends OnlCgformHead {
    @Schema(description = "主表字段集合")
    private List<OnlCgformField> onlCgformFieldList;

    @Schema(description = "表别名")
    private String alias;

    @Schema(description = "删除字段名")
    private String deletedKey;

    @Schema(description = "删除字段值")
    private String deletedValue;

    @Schema(description = "未删除字段值")
    private String notDeletedValue;

}
