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
@Schema(name = "流程定义")
public class FlowProcess extends BaseEntity implements Serializable {

    @Schema(description = "表单ID")
    private String flowId;

    @Schema(description = "表单名称")
    private String name;

    @Schema(description = "图标配置")
    private String logo;

    @Schema(description = "设置项")
    private String settings;

    @Schema(description = "分组ID")
    private Long groupId;

    @Schema(description = "表单设置内容")
    private String formItems;

    @Schema(description = "流程设置内容")
    private String process;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "0 正常 1=隐藏")
    private Boolean hidden;

    @Schema(description = "0 正常 1=停用")
    private Boolean stop;

    @Schema(description = "流程管理员")
    private Long adminId;

    @Schema(description = "唯一性id")
    private String uniqueId;

    @Schema(description = "管理员")
    private String adminList;

    @Schema(description = "范围描述显示")
    private String rangeShow;
}
