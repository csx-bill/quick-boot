package com.quick.boot.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.boot.modules.system.entity.SysProjects;
import com.quick.boot.modules.system.req.ProjectsPageParams;
import com.quick.boot.modules.system.vo.SysProjectsVO;
import org.apache.ibatis.annotations.Param;

public interface SysProjectsMapper extends BaseMapper<SysProjects> {

    IPage<SysProjectsVO> projectsPage(Page page, @Param("query") ProjectsPageParams params);

}
