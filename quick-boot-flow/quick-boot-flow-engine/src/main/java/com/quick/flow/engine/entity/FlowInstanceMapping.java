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
@Schema(name = "父子流程实例映射")
public class FlowInstanceMapping extends BaseEntity {

    private String flowInstanceId;
    private String nodeInstanceId;
    private String nodeKey;
    private String subFlowInstanceId;
    private Integer type;
    private Integer archive = 0;
    private String tenantId;
}
