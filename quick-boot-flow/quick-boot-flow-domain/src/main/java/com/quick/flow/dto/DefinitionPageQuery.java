package com.quick.flow.dto;

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
@Schema(name = "查询-流程定义接口请求参数校验")
public class DefinitionPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "流程编码")
    private String flowCode;
    @Schema(description = "流程名称")
    private String flowName;
    @Schema(description = "流程版本")
    private String version;

}
