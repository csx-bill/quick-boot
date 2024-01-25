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
@Schema(name = "流程抄送数据")
public class FlowProcessCopy extends BaseEntity implements Serializable {

    @Schema(description = "流程发起时间")
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime startTime;

    @Schema(description = "当前节点时间")
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime nodeTime;

    @Schema(description = "发起人")
    private Long startUserId;

    @Schema(description = "流程id")
    private String flowId;

    @Schema(description = "实例id")
    private String processInstanceId;

    @Schema(description = "节点id")
    private String nodeId;

    @Schema(description = "分组id")
    private Long groupId;

    @Schema(description = "分组名称")
    private String groupName;

    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "节点名称")
    private String nodeName;

    @Schema(description = "表单数据")
    private String formData;

    @Schema(description = "抄送人id")
    private Long userId;

}
