package com.quik.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UpdateProjectUserParams {

    @Schema(description = "用户类型: 管理员 admin,普通用户 user")
    @NotBlank
    private String userType;

    @Schema(description = "角色列表")
    private String roles;
}
