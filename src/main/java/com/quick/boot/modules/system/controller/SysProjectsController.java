package com.quick.boot.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.quick.boot.modules.common.vo.ApiPage;
import com.quick.boot.modules.common.vo.R;
import com.quick.boot.modules.system.entity.SysProjects;
import com.quick.boot.modules.system.req.ProjectsPageParams;
import com.quick.boot.modules.system.req.SaveProjectsParams;
import com.quick.boot.modules.system.service.SysProjectsService;
import com.quick.boot.modules.system.vo.SysProjectsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/projects")
@Tag(description = "projects" , name = "项目管理" )
public class SysProjectsController {
    private final SysProjectsService sysProjectsService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询" , description = "分页查询" )
    public R<IPage<SysProjectsVO>> page(@ParameterObject ApiPage page, @ParameterObject ProjectsPageParams params) {
        return R.ok(sysProjectsService.projectsPage(page, params));
    }

    /**
     * 新增
     * @param params 新增参数
     * @return R
     */
    @PostMapping
    @Operation(summary = "新增" , description = "新增" )
    public R<Boolean> saveProjects(@RequestBody SaveProjectsParams params) {
        return R.ok(sysProjectsService.saveProjects(params));
    }


    /**
     * 通过id更新
     * @param params 修改参数
     * @return R
     */
    @PutMapping("/{id}")
    @Operation(summary = "通过id更新" , description = "通过id更新" )
    public R<Boolean> updateById(@PathVariable("id") Long id,@RequestBody SaveProjectsParams params) {
        SysProjects sysProjects = new SysProjects();
        BeanUtils.copyProperties(params,sysProjects);
        sysProjects.setId(id);
        return R.ok(sysProjectsService.updateById(sysProjects));
    }

    /**
     * 通过id删除
     * @param id 删除参数
     * @return R
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "通过id删除" , description = "通过id删除" )
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(sysProjectsService.removeById(id));
    }

    /**
     * 通过id查询
     * @param id 查询参数
     * @return R
     */
    @GetMapping("/{id}" )
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    public R<SysProjects> getById(@PathVariable("id") Long id) {
        return R.ok(sysProjectsService.getById(id));
    }

}
