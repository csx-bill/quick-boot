package com.quick.boot.modules.common.security;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 自定义用户权限实现类
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回账号所拥有项目的权限码集合
     * @param loginId
     * @param loginType
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return (List<String>) StpUtil.getTerminalInfo().getExtra("perms");
    }

    /**
     * 返回账号所拥有项目的角色标识集合
     * @param loginId
     * @param loginType
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return (List<String>) StpUtil.getTerminalInfo().getExtra("roles");
    }
}
