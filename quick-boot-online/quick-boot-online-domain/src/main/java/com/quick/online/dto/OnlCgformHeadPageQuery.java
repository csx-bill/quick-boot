package com.quick.online.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "查询")
public class OnlCgformHeadPageQuery {
    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "表描述")
    private String tableTxt;
}
