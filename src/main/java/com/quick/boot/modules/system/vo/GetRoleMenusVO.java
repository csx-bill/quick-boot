package com.quick.boot.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
@Data
public class GetRoleMenusVO {
    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "菜单列表")
    private String menuIds;
}
