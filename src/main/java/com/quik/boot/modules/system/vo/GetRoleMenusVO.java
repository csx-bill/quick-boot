package com.quik.boot.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
@Data
public class GetRoleMenusVO {
    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单列表")
    private List<Long> menuIds;
}
