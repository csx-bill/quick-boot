package com.quick.boot.modules.system.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.quick.boot.modules.common.vo.R;
import com.quick.boot.modules.common.vo.UserInfo;
import com.quick.boot.modules.system.entity.SysUsers;
import com.quick.boot.modules.system.req.LoginReq;
import com.quick.boot.modules.system.service.SysRolesService;
import com.quick.boot.modules.system.service.SysUsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(description = "auth" , name = "认证管理" )
public class LoginController {
    private final SysUsersService sysUsersService;
    private final SysRolesService sysRolesService;

    // 登录接口
    @PostMapping("/login")
    public R<UserInfo> login(@RequestBody LoginReq loginReq) {
        SysUsers sysUser = sysUsersService.lambdaQuery().eq(SysUsers::getUsername, loginReq.getUsername()).one();
        if (Objects.isNull(sysUser) || !sysUser.getPassword().equals(loginReq.getPassword())) {
            return R.failed("登录失败");
        }

        List<String> roles = sysRolesService.getUserRoles(sysUser.getId());
        List<String> perms = sysRolesService.getRolesPermissions(sysUser.getId());
        SaLoginParameter extras = new SaLoginParameter()
                .setTerminalExtra("username", sysUser.getUsername())
                .setTerminalExtra("roles", roles)
                .setTerminalExtra("perms", perms);

        // 根据账号id，进行登录
        StpUtil.login(sysUser.getId(), extras);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(sysUser.getId());
        userInfo.setUsername(sysUser.getUsername());
        userInfo.setAvatar(sysUser.getAvatar());
        userInfo.setAccessToken(tokenInfo.getTokenValue());
        return R.ok(userInfo);
    }

    // 退出登录接口
    @PostMapping("/logout")
    public R<UserInfo> logout() {
        StpUtil.logout();
        return R.ok();
    }
}
