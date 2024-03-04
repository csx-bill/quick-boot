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
@Schema(name = "流程实例数据")
public class FlowInstanceData extends BaseEntity {
    private String flowInstanceId;
    private String instanceDataId;
    private String nodeInstanceId;
    private String flowDeployId;
    private String flowModuleId;
    private String nodeKey;
    private String instanceData;
    private String instanceDataEncode;
    private Integer type;
    private Integer archive = 0;
    private String tenantId;

}
