package com.quick.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.system.entity.SysUserRole;
import com.quick.system.mapper.SysUserRoleMapper;
import com.quick.system.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
    @Override
    public Boolean batchAuthorizedUser(Long roleId, String userIds) {
        String[] idArray = userIds.split(",");
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (String userId : idArray) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(Long.parseLong(userId));
            sysUserRole.setRoleId(roleId);
            userRoleList.add(sysUserRole);
        }
        return saveBatch(userRoleList);
    }
}
