package com.quick.modules.online.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "同步数据库表信息参数")
public class SyncReq {
    @Schema(description = "表名称，多个用,分割")
    String tableNames;
}
