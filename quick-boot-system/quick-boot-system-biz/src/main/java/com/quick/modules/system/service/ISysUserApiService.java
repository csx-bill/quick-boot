package com.quick.modules.system.service;

import com.quick.modules.system.entity.SysUser;

public interface ISysUserApiService {
    SysUser findByUsername(String username);
}
