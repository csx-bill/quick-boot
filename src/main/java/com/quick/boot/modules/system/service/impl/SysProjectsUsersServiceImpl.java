package com.quick.boot.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.boot.modules.common.exception.CheckedException;
import com.quick.boot.modules.system.entity.SysProjectsUsers;
import com.quick.boot.modules.system.entity.SysUserRole;
import com.quick.boot.modules.system.mapper.SysProjectsUsersMapper;
import com.quick.boot.modules.system.mapper.SysUserRoleMapper;
import com.quick.boot.modules.system.req.ProjectsUsersPageParams;
import com.quick.boot.modules.system.req.SaveProjectUserParams;
import com.quick.boot.modules.system.req.UpdateProjectUserParams;
import com.quick.boot.modules.system.service.SysProjectsUsersService;
import com.quick.boot.modules.system.vo.ProjectsUsersDetailsVO;
import com.quick.boot.modules.system.vo.ProjectsUsersVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class SysProjectsUsersServiceImpl extends ServiceImpl<SysProjectsUsersMapper, SysProjectsUsers> implements SysProjectsUsersService {

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<ProjectsUsersVO> projectsUsersPage(Page page, ProjectsUsersPageParams params) {
       return baseMapper.projectsUsersPage(page,params);
    }

    /**
     * 保存用户关联信息（项目关联和角色关联）
     * @param params 保存用户参数对象，包含用户ID、项目ID、用户类型和角色列表
     * @return 操作是否成功，始终返回true
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveProjectUser(SaveProjectUserParams params) {
        SysProjectsUsers checkUser = getOne(Wrappers.<SysProjectsUsers>lambdaQuery()
                .eq(SysProjectsUsers::getProjectId, params.getProjectId())
                .eq(SysProjectsUsers::getUserId, params.getUserId())
        );

        if(Objects.nonNull(checkUser)){
            throw new CheckedException("用户已存在该项目中");
        }

        // 1. 保存用户项目关联信息
        SysProjectsUsers sysUserProject = new SysProjectsUsers();
        sysUserProject.setUserId(params.getUserId());
        sysUserProject.setProjectId(params.getProjectId());
        sysUserProject.setUserType(params.getUserType());
        baseMapper.insert(sysUserProject);

        // 2. 如果是普通用户类型，保存用户角色关联信息
        if ("user".equals(params.getUserType())) {
            String[] roles = params.getRoles().split(",");
            for (String roleId : roles) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(params.getUserId());
                sysUserRole.setRoleId(Long.parseLong(roleId));
                sysUserRole.setProjectId(params.getProjectId());
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProjectUser(Long id, UpdateProjectUserParams params) {
        // 1. 校验用户是否存在（使用更清晰的查询方式）
        SysProjectsUsers existingUser = getById(id);
        if (Objects.isNull(existingUser)) {
            throw new CheckedException("用户不存在");
        }

        // 2. 更新用户类型（仅更新必要字段）
        SysProjectsUsers updateEntity = new SysProjectsUsers();
        updateEntity.setId(existingUser.getId());
        updateEntity.setUserType(params.getUserType());
        updateById(updateEntity);

        // 清除角色关联数据
        sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, existingUser.getUserId()));

        // 3. 如果是普通用户类型，保存用户角色关联信息
        if ("user".equals(params.getUserType())) {
            String[] roles = params.getRoles().split(",");
            for (String roleId : roles) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(existingUser.getUserId());
                sysUserRole.setRoleId(Long.parseLong(roleId));
                sysUserRoleMapper.insert(sysUserRole);
            }
        }

        return true;
    }

    @Override
    public ProjectsUsersDetailsVO getProjectUser(Long id) {
        return baseMapper.findUserByProjectsUsersId(id);
    }

}
