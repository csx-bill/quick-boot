package com.quick.flow.entity;

import com.quick.common.entity.BaseEntity;
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
@Schema(name = "流程执行实例表")
public class FlowInstance extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @Schema(description = "流程执行实例ID")
    private String flowInstanceId;

    @Schema(description = "父流程执行实例ID")
    private String parentFlowInstanceId;

    @Schema(description = "流程模型部署ID")
    private String flowDeployId;

    @Schema(description = "流程模型ID")
    private String flowModuleId;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "归档状态")
    private String archive;

    @Schema(description = "调用方")
    private String caller;

}
