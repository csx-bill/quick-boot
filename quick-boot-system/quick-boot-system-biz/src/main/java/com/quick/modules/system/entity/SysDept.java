package com.quick.modules.system.entity;

import com.quick.common.aspect.annotation.Dict;
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
@Schema(name = "部门信息")
public class SysDept extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "父部门ID")
    private String parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Dict(dictCode = "sys_dept_status")
    @Schema(description = "状态")
    private String status;

}
