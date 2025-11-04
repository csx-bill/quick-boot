package com.quik.boot.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单表
 */
@Data
public class SysMenus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "上级菜单ID")
    private Long parentId;

    @Schema(description = "菜单类型")
    private String menuType;

    /**
     * 是否启用，0禁用，1启用
     */
    @Schema(description = "状态")
    private String status;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "页面ID")
    private Long pageId;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "排序值，越小越靠前")
    private Integer sortOrder;

    /**
     * 是否可见，0隐藏，1显示
     */
    @Schema(description = "菜单是否显示")
    private String visible;

}
