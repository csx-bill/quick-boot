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
@Schema(name = "流程记录")
public class FlowProcessInstanceRecord extends BaseEntity implements Serializable {


    @Schema(name = "流程名字")
    private String name;

    @Schema(name = "图标")
    private String logo;

    @Schema(name = "用户id")
    private Long userId;

    @Schema(name = "流程id")
    private String flowId;

    @Schema(name = "流程实例id")
    private String processInstanceId;

    @Schema(name = "表单数据")
    private String formData;

    @Schema(name = "组id")
    private Long groupId;

    @Schema(name = "组名称")
    private String groupName;

    @Schema(name = "状态")
    private Integer status;

    @Schema(name = "结束时间")
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime endTime;

    @Schema(name = "上级流程实例id")
    private String parentProcessInstanceId;

}
