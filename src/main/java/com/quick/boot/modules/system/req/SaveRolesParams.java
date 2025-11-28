package com.quick.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaveRolesParams {

    @Schema(description = "角色名称")
    @NotBlank
    private String roleName;

    @Schema(description = "角色描述")
    private String roleDescription;

    @Schema(description = "项目ID")
    @NotNull
    private Long projectId;
}
