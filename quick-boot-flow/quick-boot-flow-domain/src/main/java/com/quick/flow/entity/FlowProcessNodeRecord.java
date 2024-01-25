package com.quick.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.common.constant.CommonConstant;
import com.quick.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "流程节点数据")
public class FlowProcessNodeRecord extends BaseEntity implements Serializable {

    @Schema(name = "流程id")
    private String flowId;

    @Schema(name = "流程实例id")
    private String processInstanceId;

    @Schema(name = "表单数据")
    private String data;

    @Schema(name = "节点id")
    private String nodeId;

    @Schema(name = "节点类型")
    private String nodeType;

    @Schema(name = "节点名字")
    private String nodeName;

    @Schema(name = "节点状态")
    private Integer status;

    @Schema(name = "开始时间")
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime startTime;

    @Schema(name = "结束时间")
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime endTime;

    @Schema(name = "执行id")
    private String executionId;

}
