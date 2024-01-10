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
@Schema(name = "流程实例数据表")
public class FlowInstanceData extends BaseEntity implements Serializable {
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

    @Schema(description = "实例数据ID")
    private String instanceDataId;

    @Schema(description = "节点执行实例ID")
    private String nodeInstanceId;

    @Schema(description = "节点唯一标识")
    private String nodeKey;

    @Schema(description = "数据列表json")
    private String instanceData;

//    private String instanceDataEncode;

    @Schema(description = "操作类型(1.实例初始化 2.系统执行 3.系统主动获取 4.上游更新 5.任务提交 6.任务撤回)")
    private Integer type;

}
