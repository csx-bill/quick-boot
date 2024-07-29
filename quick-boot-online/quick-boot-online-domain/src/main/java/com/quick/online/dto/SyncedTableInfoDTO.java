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
@Schema(name = "同步数据库表信息")
public class SyncedTableInfoDTO {
    @Schema(description = "数据源")
    String dsName;
    @Schema(description = "表名称，多个用,分割")
    String tableNames;
}
