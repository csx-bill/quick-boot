package com.quick.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysUserRole;

public interface ISysUserRoleService extends IService<SysUserRole> {
    Boolean batchAuthorizedUser(Long roleId,String userIds);
}
