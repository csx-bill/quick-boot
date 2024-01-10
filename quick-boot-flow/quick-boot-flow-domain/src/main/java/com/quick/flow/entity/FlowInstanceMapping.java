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
@Schema(name = "父子流程实例映射表")
public class FlowInstanceMapping extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "流程执行实例ID")
    private String flowInstanceId;

    @Schema(description = "流程节点执行实例ID")
    private String nodeInstanceId;

    @Schema(description = "节点唯一标识")
    private String nodeKey;

    @Schema(description = "子流程执行实例id")
    private String subFlowInstanceId;

    @Schema(description = "归档状态")
    private String archive;

    @Schema(description = "调用方")
    private String caller;

    @Schema(description = "状态(1.执行 2.回滚)")
    private Integer status;

}
