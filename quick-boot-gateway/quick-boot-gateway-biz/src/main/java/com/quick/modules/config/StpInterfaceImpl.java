package com.quick.modules.config;

import cn.dev33.satoken.stp.StpInterface;
import com.quick.common.vo.Result;
import com.quick.system.api.ISysUserApi;
import com.quick.system.api.dto.SysUserRoleApiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private ISysUserApi sysUserApi;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        List<String> permissionList = new ArrayList<>();
        List<String> roleList = getRoleList(loginId, loginType);
        for (String roleId : roleList) {
            Result<List<String>> result = sysUserApi.getRolePermission(roleId);
            List<String> list = result.getData();
            permissionList.addAll(list);
        }
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        Result<List<SysUserRoleApiDTO>> result = sysUserApi.getUserRole(loginId.toString());
        List<SysUserRoleApiDTO> roleList = result.getData();
        return roleList.stream().map(SysUserRoleApiDTO::getRoleId).collect(Collectors.toList());
    }
}
