package com.quick.modules.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Schema(name = "页面配置信息")
public class SysMenuSchema extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "页面配置")
    private String schemaJson;

    @Schema(description = "菜单id")
    private String menuId;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "版本")
    private String version;

    @Dict(dictCode = "sys_menu_schema_status")
    @Schema(description = "状态")
    private Integer status;

    @JsonIgnore
    @Schema(description = "删除状态")
    private Integer delFlag;
}
