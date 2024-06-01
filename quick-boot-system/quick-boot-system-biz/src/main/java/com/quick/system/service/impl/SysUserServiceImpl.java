package com.quick.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.common.entity.BaseEntity;
import com.quick.common.exception.BizException;
import com.quick.common.util.SuperAdminUtils;
import com.quick.system.entity.*;
import com.quick.system.mapper.SysUserMapper;
import com.quick.system.req.AuthorizedUserPageParam;
import com.quick.system.service.*;
import com.quick.system.vo.UserInfoVO;
import com.quick.system.vo.UserPermissionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    final ISysMenuService sysMenuService;
    final ISysDeptService sysDeptService;
    final ISysUserTenantService sysUserTenantService;
    final ISysTenantService sysTenantService;

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
        pageVo.getRecords().stream().forEach(record -> {
            // 将每个对象的密码设置为null
            record.setPassword(null);
            // 赋值部门名称
            if(Optional.ofNullable(record.getDeptId()).isPresent()){
                SysDept sysDept = sysDeptService.getById(record.getDeptId());
                if(Optional.ofNullable(sysDept).isPresent()){
                    record.setDeptName(sysDept.getName());
                }
            }
        });
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
        checkUserAllowed((Long)id);
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object id : list) {
            checkUserAllowed((Long)id);
        }
        return super.removeByIds(list);
    }

    /**
     * 校验用户是否允许操作
     *
     */
    @Override
    public void checkUserAllowed(Long userId) {
        if (SuperAdminUtils.isSuperAdmin(userId)) {
            throw new BizException(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,"不允许操作超级管理员用户");
        }
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        SysUser sysUser = getById(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(sysUser, userInfoVO);
        userInfoVO.setUserId(userId);

        // 查询当前登录用户的租户
        List<SysUserTenant> list = sysUserTenantService.list(new LambdaQueryWrapper<SysUserTenant>()
                .eq(SysUserTenant::getUserId, userId));
        List<Long> tenantIds = list.stream().map(SysUserTenant::getTenantId).collect(Collectors.toList());
        List<SysTenant> userTenant = sysTenantService.list(new LambdaQueryWrapper<SysTenant>().in(BaseEntity::getId, tenantIds));
        userInfoVO.setUserTenant(userTenant);

        return userInfoVO;
    }

    @Override
    public UserPermissionVO getUserPermission(Long userId) {
        UserPermissionVO vo = new UserPermissionVO();
        // 按钮权限
        List<String> permsCode = sysMenuService.getUserButton(userId);
        vo.setPermsCode(permsCode);
        List<SysMenu> userMenu = sysMenuService.getUserMenu(userId);
        vo.setUserMenuTree(userMenu);
        return vo;
    }

    @Override
    public List<SysTenant> getUserTenantList() {
        // 查询当前登录用户的租户
        List<SysUserTenant> list = sysUserTenantService.list(new LambdaQueryWrapper<SysUserTenant>()
                .eq(SysUserTenant::getUserId, StpUtil.getLoginIdAsLong()));
        List<Long> tenantIds = list.stream().map(SysUserTenant::getTenantId).collect(Collectors.toList());
        List<SysTenant> userTenant = sysTenantService.list(new LambdaQueryWrapper<SysTenant>().in(BaseEntity::getId, tenantIds));
        return userTenant;
    }
}
