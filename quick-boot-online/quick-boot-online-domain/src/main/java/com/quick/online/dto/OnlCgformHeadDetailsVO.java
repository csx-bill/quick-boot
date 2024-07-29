package com.quick.online.dto;

import com.quick.online.entity.Access;
import com.quick.online.entity.OnlCgformField;
import com.quick.online.entity.OnlCgformHead;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class OnlCgformHeadDetailsVO extends OnlCgformHead {
    @Schema(description = "主表字段集合")
    private List<OnlCgformField> onlCgformFieldList;

    @Schema(description = "附表集合详情")
    private List<OnlCgformHeadDetailsVO> subOnlCgformHeadDetailsVOList;

    @Schema(description = "表别名")
    private String alias;

}
