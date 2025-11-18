package com.quik.boot.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quik.boot.modules.common.vo.ApiPage;
import com.quik.boot.modules.common.vo.R;
import com.quik.boot.modules.system.entity.SysPages;
import com.quik.boot.modules.system.req.PagesParams;
import com.quik.boot.modules.system.req.SavePagesParams;
import com.quik.boot.modules.system.req.UpdatePagesParams;
import com.quik.boot.modules.system.service.SysPagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/pages")
@Tag(description = "pages" , name = "页面管理" )
public class SysPagesController {
    private final SysPagesService sysPagesService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询" , description = "分页查询" )
    public R<IPage<SysPages>> page(@ParameterObject ApiPage page, @ParameterObject @Valid PagesParams params) {
        LambdaQueryWrapper<SysPages> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysPages::getProjectId,params.getProjectId());
        wrapper.like(StringUtils.hasText(params.getPageName()), SysPages::getPageName,params.getPageName());
        return R.ok(sysPagesService.page(page,wrapper));
    }

    /**
     * 列表查询
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @Operation(summary = "列表查询" , description = "列表查询" )
    public R<List<SysPages>> list(@ParameterObject PagesParams params) {
        LambdaQueryWrapper<SysPages> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysPages::getProjectId,params.getProjectId());
        wrapper.like(StringUtils.hasText(params.getPageName()), SysPages::getPageName,params.getPageName());
        return R.ok(sysPagesService.list(wrapper));
    }

    /**
     * 新增
     * @param params 新增参数
     * @return R
     */
    @PostMapping
    @Operation(summary = "新增" , description = "新增" )
    public R<Boolean> saveProjects(@RequestBody SavePagesParams params) {
        SysPages sysPages = new SysPages();
        BeanUtils.copyProperties(params,sysPages);
        return R.ok(sysPagesService.save(sysPages));
    }


    /**
     * 通过id更新
     * @param params 修改参数
     * @return R
     */
    @PutMapping("/{id}")
    @Operation(summary = "通过id更新" , description = "通过id更新" )
    public R<Boolean> updateById(@PathVariable("id") Long id,@RequestBody UpdatePagesParams params) {
        SysPages sysPages = new SysPages();
        BeanUtils.copyProperties(params,sysPages);
        sysPages.setId(id);
        return R.ok(sysPagesService.updateById(sysPages));
    }


    /**
     * 批量删除
     * @return R
     */
    @DeleteMapping
    @Operation(summary = "批量删除" , description = "批量删除" )
    public R<Boolean> removeBatchByIds(@RequestParam("ids") String ids) {
        return R.ok(sysPagesService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    /**
     * 通过id删除
     * @param id 删除参数
     * @return R
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "通过id删除" , description = "通过id删除" )
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(sysPagesService.removeById(id));
    }

    /**
     * 通过id查询
     * @param id 查询参数
     * @return R
     */
    @GetMapping("/{id}" )
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    public R<SysPages> getById(@PathVariable("id") Long id) {
        return R.ok(sysPagesService.getById(id));
    }

}
