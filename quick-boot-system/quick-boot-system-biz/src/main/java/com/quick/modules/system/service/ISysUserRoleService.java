package com.quick.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.system.entity.SysUserRole;

public interface ISysUserRoleService extends IService<SysUserRole> {
    Boolean batchAuthorizedUser(String roleId,String userIds);
}
