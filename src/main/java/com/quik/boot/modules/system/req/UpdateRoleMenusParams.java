package com.quik.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRoleMenusParams {

    @Schema(description = "角色ID")
    @NotNull
    private Long id;

    @Schema(description = "菜单列表")
    private String menuIds;

}
