package com.quick.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "角色权限")
public class RolePermissionsVO {
    @Schema(description = "权限ID")
    private String permissions;
    @Schema(description = "角色ID")
    private Long roleId;
}
