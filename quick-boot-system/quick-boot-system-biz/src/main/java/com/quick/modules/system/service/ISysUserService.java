package com.quick.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.system.entity.SysUser;
import com.quick.modules.system.req.AuthorizedUserPageParam;
import com.quick.modules.system.vo.UserInfoVO;

public interface ISysUserService extends IService<SysUser> {
    IPage<SysUser> authorizedUserPage(Page<SysUser> page, AuthorizedUserPageParam param);
    IPage<SysUser> unauthorizedUserPage(Page<SysUser> page, AuthorizedUserPageParam param);
    void checkUserAllowed(String userId);
    UserInfoVO getUserInfo(String userId);
}
