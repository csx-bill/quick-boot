package com.quick.system.req;

import com.quick.common.util.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "用户信息分页查询参数")
public class SysUserPageParam extends PageParam {

    @Schema(description = "账号")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;
}
