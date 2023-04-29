package com.quick.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.system.entity.SysRole;
import com.quick.modules.system.vo.RolePermissionsVO;

public interface ISysRoleService extends IService<SysRole> {
    RolePermissionsVO getRolePermissions(String id);
    Boolean saveRolePermissions(RolePermissionsVO vo);
    void checkRoleAllowed(String roleId);

}
