package com.quick.system.vo;

import com.quick.common.aspect.annotation.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "在线用户信息")
public class SysUserOnlineVO {

    @Schema(description = "账号")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "会话")
    private String token;


}
