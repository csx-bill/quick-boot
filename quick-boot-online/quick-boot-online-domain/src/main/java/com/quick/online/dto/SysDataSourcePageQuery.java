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
@Schema(name = "查询-数据源")
public class SysDataSourcePageQuery {
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据源名称")
    private String name;
}
