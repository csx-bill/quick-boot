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
@Schema(name = "流程节点数据")
public class FlowProcessNodeData extends BaseEntity implements Serializable {

    @Schema(name = "流程id")
    private String flowId;

    @Schema(name = "表单数据")
    private String data;

    @Schema(name = "节点id")
    private String nodeId;

}
