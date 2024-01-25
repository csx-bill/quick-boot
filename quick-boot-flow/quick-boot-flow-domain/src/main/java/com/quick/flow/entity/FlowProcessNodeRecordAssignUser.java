package com.quick.flow.entity;

import com.quick.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "流程节点记录-执行人")
public class FlowProcessNodeRecordAssignUser extends BaseEntity implements Serializable {

    @Schema(name = "流程id")
    private String flowId;

    @Schema(name = "流程实例id")
    private String processInstanceId;

    @Schema(name = "表单数据")
    private String data;

    @Schema(name = "节点id")
    private String nodeId;

    @Schema(name = "用户id")
    private String userId;

    @Schema(name = "节点状态")
    private Integer status;

    @Schema(name = "开始时间")
    private LocalDateTime startTime;

    @Schema(name = "结束时间")
    private LocalDateTime endTime;

    @Schema(name = "执行id")
    private String executionId;

    @Schema(name = "任务id")
    private String taskId;

    @Schema(name = "审批意见")
    private String approveDesc;

    @Schema(name = "节点名称")
    private String nodeName;

    @Schema(name = "任务类型")
    private String taskType;

    @Schema(name = "表单本地数据")
    private String localData;


}
