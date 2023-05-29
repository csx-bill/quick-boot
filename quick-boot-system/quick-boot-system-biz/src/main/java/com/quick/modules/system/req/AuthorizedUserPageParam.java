package com.quick.modules.system.req;

import com.quick.common.util.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "已授权用户分页查询参数")
public class AuthorizedUserPageParam extends PageParam {
    @Schema(description = "账号")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @NotBlank
    @Schema(description = "角色ID",required = true)
    private String roleId;
}
