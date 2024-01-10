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
@Schema(name = "流程节点执行记录表")
public class FlowNodeInstanceLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @Schema(description = "流程节点执行实例ID")
    private String nodeInstanceId;

    @Schema(description = "流程执行实例ID")
    private String flowInstanceId;

    @Schema(description = "实例数据ID")
    private String instanceDataId;

    @Schema(description = "节点唯一标识")
    private String nodeKey;

    @Schema(description = "操作类型(1.系统执行 2.任务提交 3.任务撤销)")
    private Integer type;

    @Schema(description = "状态(1.处理成功 2.处理中 3.处理失败 4.处理已撤销)")
    private Integer status;

    @Schema(description = "归档状态")
    private String archive;

    @Schema(description = "调用方")
    private String caller;

}
