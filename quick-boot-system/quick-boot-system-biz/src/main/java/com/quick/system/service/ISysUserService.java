package com.quick.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysTenant;
import com.quick.system.entity.SysUser;
import com.quick.system.req.AuthorizedUserPageParam;
import com.quick.system.vo.UserInfoVO;

import java.util.List;

public interface ISysUserService extends IService<SysUser> {
    IPage<SysUser> authorizedUserPage(Page<SysUser> page, AuthorizedUserPageParam param);
    IPage<SysUser> unauthorizedUserPage(Page<SysUser> page, AuthorizedUserPageParam param);
    void checkUserAllowed(Long userId);
    UserInfoVO getUserInfo(Long userId);

    List<SysTenant> getUserTenantList();
}
