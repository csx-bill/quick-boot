package com.quick.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.constant.CommonConstant;
import com.quick.common.exception.BizException;
import com.quick.common.util.SuperAdminUtils;
import com.quick.system.entity.SysDept;
import com.quick.system.entity.SysMenu;
import com.quick.system.entity.SysUser;
import com.quick.system.mapper.SysUserMapper;
import com.quick.system.req.AuthorizedUserPageParam;
import com.quick.system.service.ISysDeptService;
import com.quick.system.service.ISysMenuService;
import com.quick.system.service.ISysUserService;
import com.quick.system.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    final ISysMenuService sysMenuService;
    final ISysDeptService sysDeptService;

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
        // 按钮权限
        List<String> permsCode = sysMenuService.getUserButton(userId);
        userInfoVO.setPermsCode(permsCode);
        List<SysMenu> userMenuTree = sysMenuService.getUserMenuTree(userId);
        userInfoVO.setUserMenuTree(userMenuTree);
        return userInfoVO;
    }
}
