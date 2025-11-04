package com.quik.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RolesPageParams {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "项目ID")
    @NotNull
    private Long projectId;
}
