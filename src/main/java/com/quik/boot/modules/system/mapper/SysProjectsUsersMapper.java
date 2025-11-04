package com.quik.boot.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quik.boot.modules.system.entity.SysProjectsUsers;
import com.quik.boot.modules.system.req.ProjectsUsersPageParams;
import com.quik.boot.modules.system.vo.ProjectsUsersDetailsVO;
import com.quik.boot.modules.system.vo.ProjectsUsersVO;
import org.apache.ibatis.annotations.Param;

public interface SysProjectsUsersMapper extends BaseMapper<SysProjectsUsers> {
    IPage<ProjectsUsersVO> projectsUsersPage(Page page,@Param("query")ProjectsUsersPageParams params);
    ProjectsUsersDetailsVO  findUserByProjectsUsersId(@Param("id")Long id);

}
