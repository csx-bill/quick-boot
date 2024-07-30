package com.quick.system.dto;

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
@Schema(name = "更新-菜单信息")
public class SysMenuUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id;

    @Schema(description = "父ID")
    private String parentId;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单权限编码")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "菜单排序")
    private Double orderNo;

    @Schema(description = "类型")
    private String menuType;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "是否隐藏菜单")
    private String hideInMenu;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "schema")
    private String schema;

    @Schema(description = "组件")
    private String component;

}
