package com.quick.system.api.fallback;

import com.quick.common.vo.Result;
import com.quick.system.api.ISysUserApi;
import com.quick.system.api.dto.SysUserApiDTO;
import com.quick.system.api.dto.SysUserRoleApiDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SysUserApiFallback implements ISysUserApi {
    @Setter
    private Throwable cause;
    @Override
    public Result<SysUserApiDTO> findByUsername(String username) {
        log.error("请求失败 {}", cause);
        return null;
    }

    @Override
    public Result<List<SysUserRoleApiDTO>> getUserRole(String userId) {
        log.error("请求失败 {}", cause);
        return null;
    }

    @Override
    public Result<List<String>> getRolePermission(String roleId) {
        log.error("请求失败 {}", cause);
        return null;
    }
}
