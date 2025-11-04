package com.quik.boot.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectsUsersVO {
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户类型: 管理员 admin,普通用户 user")
    private String userType;

    @Schema(description = "角色列表")
    private String roleNames;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
