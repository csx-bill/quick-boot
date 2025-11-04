package com.quik.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveMenusParams {

    @Schema(description = "菜单名称")
    @NotBlank
    private String name;

    @Schema(description = "上级菜单ID")
    private Long parentId=-1L;

    @Schema(description = "菜单类型")
    @NotBlank
    private String menuType;


    @Schema(description = "状态")
    @NotBlank
    private String status;

    @Schema(description = "项目ID")
    @NotNull
    private Long projectId;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "页面ID")
    private Long pageId;

    @Schema(description = "权限标识")
    private String permission;
}
