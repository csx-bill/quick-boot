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
@Schema(name = "流程节点执行记录表")
public class FlowNodeInstanceLog extends BaseEntity {
    private String nodeInstanceId;
    private String flowInstanceId;
    private String instanceDataId;
    private String nodeKey;
    private Integer type;
    private Integer status;
    private Integer archive = 0;
    private String tenantId;
}
