package com.quick.boot.modules.common.config.magic;

import cn.dev33.satoken.session.SaTerminalInfo;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.context.MagicUser;
import org.ssssssss.magicapi.core.exception.MagicLoginException;
import org.ssssssss.magicapi.core.interceptor.Authorization;
import org.ssssssss.magicapi.core.interceptor.AuthorizationInterceptor;
import org.ssssssss.magicapi.core.model.Group;
import org.ssssssss.magicapi.core.model.MagicEntity;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletRequest;

/**
 * 自定义用户名密码登录
 */
@Component
public class CustomAuthInterceptor implements AuthorizationInterceptor {

	/**
     * 配置是否需要登录
	 */
	@Override
	public boolean requireLogin() {
		return true;
	}

	/**
     * 根据Token获取User
	 */
	@Override
	public MagicUser getUserByToken(String token) throws MagicLoginException {
        SaTokenInfo info = StpUtil.getTokenInfo();
        SaTerminalInfo terminalInfo = StpUtil.getTerminalInfo();
        String username = "noLogin";
        if (terminalInfo != null) {
            username = (String) terminalInfo.getExtra("username");
        }
        return new MagicUser(info.loginId.toString(),username,token);
	}

    /**
     * 是否拥有页面按钮的权限
     */
    @Override
    public boolean allowVisit(MagicUser magicUser, MagicHttpServletRequest request, Authorization authorization) {
        return StpUtil.hasPermission(authorization.name().toLowerCase());
    }

    /**
     * 是否拥有对该接口的增删改权限
     */
    @Override
    public boolean allowVisit(MagicUser magicUser, MagicHttpServletRequest request, Authorization authorization, MagicEntity entity) {
        return allowVisit(magicUser, request, authorization);
    }

    /**
     * 是否拥有对该接口的增删改权限
     */
    @Override
    public boolean allowVisit(MagicUser magicUser, MagicHttpServletRequest request, Authorization authorization, Group group) {
        return allowVisit(magicUser, request, authorization);
    }

    @Override
    public void logout(String token) {
        StpUtil.logout();
    }
}