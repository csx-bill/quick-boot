package com.quick.boot.modules.common.security;

import cn.dev33.satoken.stp.StpInterface;
import com.quick.boot.modules.system.entity.SysRoles;
import com.quick.boot.modules.system.service.SysRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义用户权限实现类
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private SysRolesService sysRolesService;

    /**
     * 返回账号所拥有项目的权限码集合
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return sysRolesService.getRolesPermissions();
    }

    /**
     * 返回账号所拥有项目的角色标识集合
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return sysRolesService.getUserRoles();
    }
}
