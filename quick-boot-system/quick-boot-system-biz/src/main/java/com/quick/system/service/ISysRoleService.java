package com.quick.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysRole;
import com.quick.system.vo.RolePermissionsVO;

public interface ISysRoleService extends IService<SysRole> {
    RolePermissionsVO getRolePermissions(Long id);
    Boolean saveRolePermissions(RolePermissionsVO vo);
    void checkRoleAllowed(Long roleId);

}
