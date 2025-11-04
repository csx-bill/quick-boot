package com.quik.boot.modules.system.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quik.boot.modules.system.entity.SysProjectsUsers;
import com.quik.boot.modules.system.req.ProjectsUsersPageParams;
import com.quik.boot.modules.system.req.SaveProjectUserParams;
import com.quik.boot.modules.system.req.UpdateProjectUserParams;
import com.quik.boot.modules.system.vo.ProjectsUsersDetailsVO;
import com.quik.boot.modules.system.vo.ProjectsUsersVO;

public interface SysProjectsUsersService extends IService<SysProjectsUsers> {
    IPage<ProjectsUsersVO> projectsUsersPage(Page page, ProjectsUsersPageParams params);

    Boolean saveProjectUser(SaveProjectUserParams params);
    Boolean updateProjectUser(Long id, UpdateProjectUserParams params);

    ProjectsUsersDetailsVO getProjectUser(Long id);

}
