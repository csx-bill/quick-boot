package com.quick.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.system.entity.SysUser;
import com.quick.system.req.AuthorizedUserPageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    IPage<SysUser> authorizedUserPage(Page<SysUser> page, @Param("param") AuthorizedUserPageParam param);
    IPage<SysUser> unauthorizedUserPage(Page<SysUser> page, @Param("param") AuthorizedUserPageParam param);
}
