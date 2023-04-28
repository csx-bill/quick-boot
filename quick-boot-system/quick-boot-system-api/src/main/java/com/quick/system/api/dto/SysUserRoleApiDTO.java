package com.quick.system.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRoleApiDTO implements Serializable {
    @Schema(description = "ID")
    private String id;

    @Schema(description = "角色id")
    private String roleId;

    @Schema(description = "用户id")
    private String userId;
}
