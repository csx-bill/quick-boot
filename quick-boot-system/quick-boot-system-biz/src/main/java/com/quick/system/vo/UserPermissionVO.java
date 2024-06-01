package com.quick.system.vo;

import com.quick.system.entity.SysMenu;
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
@Schema(name = "用户权限信息")
public class UserPermissionVO {
    @Schema(description = "按钮权限")
    List<String> permsCode;

    @Schema(description = "用户拥有的菜单")
    List<SysMenu> userMenuTree;
}
