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
@Schema(name = "流程定义")
public class FlowDefinition extends BaseEntity {
    private String flowModuleId;
    private String flowName;
    private String flowKey;
    private String flowModel;
    private Integer status;
    private Integer archive = 0;
    private String remark;
    private String tenantId;
}
