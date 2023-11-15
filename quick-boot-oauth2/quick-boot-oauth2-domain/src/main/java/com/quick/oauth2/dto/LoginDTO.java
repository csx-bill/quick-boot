package com.quick.oauth2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "登录实体")
public class LoginDTO {
    @Schema(description = "账号")
    private String username;
    @Schema(description = "密码")
    private String password;
}
