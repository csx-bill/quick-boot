package com.quik.boot.modules.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户信息
 */
@Data
public class UserInfo {
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "用户账号")
    private String username;
    @Schema(description = "accessToken")
    private String accessToken;
}
