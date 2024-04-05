package com.quick.flow.dto;

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
@Schema(name = "查询-已办任务接口请求参数校验")
public class DoneTaskPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务名称")
    private String nodeName;

    @Schema(description = "审批人")
    private String createBy;

    @Schema(description = "流程状态")
    private Integer flowStatus;

}
