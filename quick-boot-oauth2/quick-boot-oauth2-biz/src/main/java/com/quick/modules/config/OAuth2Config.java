package com.quick.modules.config;

import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.exception.UsernameNotFoundException;
import com.quick.common.exception.code.ExceptionCode;
import com.quick.common.vo.Result;
import com.quick.system.api.ISysUserApi;
import com.quick.system.api.dto.SysUserApiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class OAuth2Config {
    @Lazy
    @Autowired
    private ISysUserApi sysUserApi;
    // Sa-OAuth2 定制化配置
    @Autowired
    public void setOAuth2Config(SaOAuth2Config cfg) {
        cfg.
                // 配置：未登录时返回的View
                        setNotLoginView(() -> {
                    String msg = "当前会话在SSO-Server端尚未登录，请先访问"
                            + "<a href='/oauth2/doLogin?name=sa&pwd=123456' target='_blank'> doLogin登录 </a>"
                            + "进行登录之后，刷新页面开始授权";
                    return msg;
                }).
                // 配置：登录处理函数
                        setDoLoginHandle((name, pwd) -> {
                    Result<SysUserApiDTO> result = sysUserApi.findByUsername(name);
                    SysUserApiDTO sysUserApiDTO = result.getData();
                    if(sysUserApiDTO!=null){
                        if(sysUserApiDTO.getUsername().equals(name) && BCrypt.checkpw(pwd,sysUserApiDTO.getPassword())) {
                            StpUtil.login(sysUserApiDTO.getId());
                            return Result.success();
                        }
                    }
                    throw new UsernameNotFoundException(ExceptionCode.USERNAME_NOT_FOUND.getCode(),ExceptionCode.USERNAME_NOT_FOUND.getMsg());
                }).
                // 配置：确认授权时返回的View
                        setConfirmView((clientId, scope) -> {
                    String msg = "<p>应用 " + clientId + " 请求授权：" + scope + "</p>"
                            + "<p>请确认：<a href='/oauth2/doConfirm?client_id=" + clientId + "&scope=" + scope + "' target='_blank'> 确认授权 </a></p>"
                            + "<p>确认之后刷新页面</p>";
                    return msg;
                })
        ;
    }
}
