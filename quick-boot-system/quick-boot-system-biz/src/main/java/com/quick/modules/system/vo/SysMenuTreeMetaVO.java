package com.quick.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "菜单树")
public class SysMenuTreeMetaVO {

    @Schema(description = "菜单名称")
    private String title;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "菜单排序")
    private Double orderNo;

    @Schema(description = "是否隐藏路由菜单")
    private boolean hideMenu;
}
