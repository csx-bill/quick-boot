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
@Schema(name = "查询-流程信息")
public class FlowDefinitionPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "流程名称")
    private String flowName;

}
