package com.quik.boot.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysMenusVO {
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "上级菜单ID")
    private Long parentId;

    @Schema(description = "菜单类型")
    private String menuType;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "页面ID")
    private Long pageId;

    @Schema(description = "页面名称")
    private String pageName;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "排序值，越小越靠前")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子菜单")
    private List<SysMenusVO> children;
}
