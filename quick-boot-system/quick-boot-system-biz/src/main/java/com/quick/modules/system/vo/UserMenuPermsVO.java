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
@Schema(name = "用户菜单权限和按钮权限")
public class UserMenuPermsVO {
    @Schema(description = "菜单权限")
    List<SysMenuTreeVO> menu;
    @Schema(description = "按钮权限")
    List<String> permsCode;
}
