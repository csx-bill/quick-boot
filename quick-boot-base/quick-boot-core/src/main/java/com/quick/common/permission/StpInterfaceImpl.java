package com.quick.common.permission;

import cn.dev33.satoken.stp.StpInterface;
import com.quick.common.api.ISysUserBaseApi;
import com.quick.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户权限
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Lazy
    @Autowired
    private ISysUserBaseApi sysUserBaseApi;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        List<String> permissionList = new ArrayList<>();
        List<String> roleList = getRoleList(loginId, loginType);
        for (String roleId : roleList) {
            Result<List<Long>> result = sysUserBaseApi.getUserRolePermission(Long.parseLong(roleId));
            List<Long> list = result.getData();
            permissionList.addAll(list.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList()));
        }
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        Result<List<Long>> result = sysUserBaseApi.getUserRole((Long) loginId);
        List<Long> roleList = result.getData();
        return roleList.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }
}
