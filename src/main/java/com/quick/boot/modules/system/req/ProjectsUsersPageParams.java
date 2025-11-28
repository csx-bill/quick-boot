package com.quick.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectsUsersPageParams {
    @Schema(description = "项目ID")
    @NotNull
    private Long projectId;

    @Schema(description = "用户名")
    private String username;
}
