package com.quick.boot.modules.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.boot.modules.system.entity.SysProjects;
import com.quick.boot.modules.system.entity.SysProjectsUsers;
import com.quick.boot.modules.system.mapper.SysProjectsMapper;
import com.quick.boot.modules.system.mapper.SysProjectsUsersMapper;
import com.quick.boot.modules.system.req.ProjectsPageParams;
import com.quick.boot.modules.system.req.SaveProjectsParams;
import com.quick.boot.modules.system.service.SysProjectsService;
import com.quick.boot.modules.system.vo.SysProjectsVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class SysProjectsServiceImpl extends ServiceImpl<SysProjectsMapper, SysProjects> implements SysProjectsService {

    private final SysProjectsUsersMapper sysProjectsUsersMapper;
    @Override
    public IPage<SysProjectsVO> projectsPage(Page page, ProjectsPageParams params) {
        Long userId = StpUtil.getLoginIdAsLong();
        params.setUserId(userId);
        return baseMapper.projectsPage(page,params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveProjects(SaveProjectsParams params) {
        SysProjects sysProjects = new SysProjects();
        sysProjects.setProjectName(params.getProjectName());
        sysProjects.setProjectDescription(params.getProjectDescription());
        save(sysProjects);
        Long userId = StpUtil.getLoginIdAsLong();
        SysProjectsUsers sysProjectsUsers = new SysProjectsUsers();
        sysProjectsUsers.setUserId(userId);
        sysProjectsUsers.setProjectId(sysProjects.getId());
        sysProjectsUsers.setUserType("admin");
        sysProjectsUsersMapper.insert(sysProjectsUsers);
        return Boolean.TRUE;
    }

    @Override
    public SysProjectsVO projectByUserIdAndProjectId(Long projectId, Long userId) {
        return baseMapper.selectProjectByUserIdAndProjectId(projectId,userId);
    }
}
