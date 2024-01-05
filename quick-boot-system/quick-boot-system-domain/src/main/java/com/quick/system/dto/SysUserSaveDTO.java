package com.quick.system.dto;

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
@Schema(name = "保存-用户信息")
public class SysUserSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "状态")
    private String status;

}
