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
@Schema(name = "角色信息")
public class SysRole  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "描述")
    private String description;

    @Dict(dictCode = "sys_role_status")
    @Schema(description = "状态")
    private Integer status;

    @Dict(dictCode = "del_flag")
    @Schema(description = "删除状态")
    private Integer delFlag;
}
