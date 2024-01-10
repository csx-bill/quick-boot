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
@Schema(name = "流程部署表")
public class FlowDeployment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "流程模型部署ID")
    private String flowDeployId;

    @Schema(description = "流程模型ID")
    private String flowModuleId;

    @Schema(description = "流程名称")
    private String flowName;

    @Schema(description = "流程业务标识")
    private String flowKey;

    @Schema(description = "表单定义")
    private String flowModel;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "操作人")
    private String operator;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "归档状态")
    private String archive;

    @Schema(description = "调用方")
    private String caller;
}
