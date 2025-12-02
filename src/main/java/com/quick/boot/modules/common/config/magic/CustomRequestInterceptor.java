package com.quick.boot.modules.common.config.magic;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.stp.StpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.interceptor.RequestInterceptor;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.Options;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletRequest;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletResponse;
import org.ssssssss.script.MagicScriptContext;

@Component
public class CustomRequestInterceptor  implements RequestInterceptor {

	/**
	 * 接口请求之前
	 * @param info	接口信息
	 * @param context	脚本变量信息
	 */
	@Override
	public Object preHandle(ApiInfo info, MagicScriptContext context, MagicHttpServletRequest request, MagicHttpServletResponse response) throws Exception {
		String role = info.getOptionValue(Options.ROLE);
        if (StringUtils.isNotBlank(role) && !StpUtil.hasRole(role)) {
            throw new NotRoleException(role);
        }
		String perm = info.getOptionValue(Options.PERMISSION);
        if (StringUtils.isNotBlank(perm) && !StpUtil.hasRole(perm)) {
            throw new NotPermissionException(perm);
        }
		return null;
	}
}