package com.quick.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenusTreeParams {
    @Schema(description = "菜单类型")
    private String menuType;
    @Schema(description = "菜单名称")
    private String name;
    @Schema(description = "上级菜单ID")
    private Long parentId;
}
