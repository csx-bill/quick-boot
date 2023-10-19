package com.quick.modules.system.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
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
@Schema(name = "菜单信息")
public class SysMenu extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "父id")
    private String parentId;

    @TableField(condition = SqlCondition.LIKE)
    @Schema(description = "菜单名称")
    private String name;

    /**
     * 菜单权限编码，例如：“SysMenu:list,SysMenu:add”,多个逗号隔开
     */
    @Schema(description = "菜单权限编码")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "菜单排序")
    private Double orderNo;

    /**
     * 菜单类型（dir：目录；menu：菜单 ；button：按钮）
     */
    @Dict(dictCode = "sys_menu_type")
    @Schema(description = "类型")
    private String menuType;

    @Schema(description = "描述")
    private String description;

    /**
     * 是否隐藏菜单: N否,Y是
     */
    @Schema(description = "是否隐藏菜单")
    private String hideInMenu;

    @Dict(dictCode = "sys_menu_status")
    @Schema(description = "状态")
    private String status;

    @Schema(description = "schema")
    private String schema;

}
