package com.quik.boot.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quik.boot.modules.system.entity.SysProjects;
import com.quik.boot.modules.system.req.ProjectsPageParams;
import com.quik.boot.modules.system.req.SaveProjectsParams;
import com.quik.boot.modules.system.vo.SysProjectsVO;

public interface SysProjectsService extends IService<SysProjects> {
    IPage<SysProjectsVO> projectsPage(Page page, ProjectsPageParams params);
    Boolean saveProjects(SaveProjectsParams params);
}
