package com.quick.modules.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.common.exception.BizException;
import com.quick.common.util.SuperAdminUtils;
import com.quick.modules.system.entity.SysUser;
import com.quick.modules.system.mapper.SysUserMapper;
import com.quick.modules.system.req.AuthorizedUserPageParam;
import com.quick.modules.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public IPage<SysUser> authorizedUserPage(Page<SysUser> page, AuthorizedUserPageParam param) {
        return baseMapper.authorizedUserPage(page,param);
    }

    @Override
    public IPage<SysUser> unauthorizedUserPage(Page<SysUser> page, AuthorizedUserPageParam param) {
        return baseMapper.unauthorizedUserPage(page,param);
    }

    @Override
    public <E extends IPage<SysUser>> E page(E page, Wrapper<SysUser> queryWrapper) {
        E pageVo = super.page(page, queryWrapper);
        pageVo.getRecords().stream().forEach(record -> record.setPassword(null)); // 将每个对象的密码设置为null
        return pageVo;
    }

    @Override
    public boolean save(SysUser entity) {
        String password = BCrypt.hashpw(entity.getPassword());
        entity.setPassword(password);
        return super.save(entity);
    }

    @Override
    public boolean updateById(SysUser entity) {
        if(StringUtils.hasText(entity.getPassword())){
            String password = BCrypt.hashpw(entity.getPassword());
            entity.setPassword(password);
        }
        if(entity.getStatus().equals("I")){
            checkUserAllowed(entity.getId());
        }
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        checkUserAllowed(id.toString());
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object id : list) {
            checkUserAllowed(id.toString());
        }
        return super.removeByIds(list);
    }

    /**
     * 校验用户是否允许操作
     *
     */
    @Override
    public void checkUserAllowed(String userId) {
        if (SuperAdminUtils.isSuperAdmin(userId)) {
            throw new BizException(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,"不允许操作超级管理员用户");
        }
    }
}
