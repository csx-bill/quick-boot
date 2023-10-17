package com.quick.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "当前用户信息")
public class UserInfoVO {
    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "真实名字")
    private String realName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "按钮权限")
    List<String> permsCode;

    @Schema(description = "用户拥有的菜单树和按钮权限")
    List<SysMenuTreeVO> userMenuTree;

}
