package com.quick.flow.engine.entity;

import com.quick.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "流程执行实例")
public class FlowInstance extends BaseEntity {
    private String flowInstanceId;
    private String flowDeployId;
    private String flowModuleId;
    private String parentFlowInstanceId;
    private Integer status;
    private Integer archive = 0;
    private String tenantId;
}
