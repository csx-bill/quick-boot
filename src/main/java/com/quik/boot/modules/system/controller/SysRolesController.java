package com.quik.boot.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quik.boot.modules.common.vo.ApiPage;
import com.quik.boot.modules.common.vo.R;
import com.quik.boot.modules.system.entity.SysRoles;
import com.quik.boot.modules.system.req.RolesPageParams;
import com.quik.boot.modules.system.req.UpdateRoleMenusParams;
import com.quik.boot.modules.system.req.SaveRolesParams;
import com.quik.boot.modules.system.req.UpdateRolesParams;
import com.quik.boot.modules.system.service.SysRolesService;
import com.quik.boot.modules.system.vo.GetRoleMenusVO;
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
@RequestMapping("/roles")
@Tag(description = "roles" , name = "角色管理" )
public class SysRolesController {
    private final SysRolesService sysRolesService;


    /**
     * 分页查询
     * @param page 分页对象
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询" , description = "分页查询" )
    public R<IPage<SysRoles>> page(@ParameterObject ApiPage page, @ParameterObject @Valid RolesPageParams params) {
        LambdaQueryWrapper<SysRoles> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRoles::getProjectId,params.getProjectId());
        wrapper.like(StringUtils.hasText(params.getRoleName()), SysRoles::getRoleName,params.getRoleName());
        return R.ok(sysRolesService.page(page,wrapper));
    }

    /**
     * 列表查询
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @Operation(summary = "列表查询" , description = "列表查询" )
    public R<List<SysRoles>> list(@ParameterObject @Valid RolesPageParams params) {
        LambdaQueryWrapper<SysRoles> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysRoles::getProjectId,params.getProjectId());
        wrapper.like(StringUtils.hasText(params.getRoleName()), SysRoles::getRoleName,params.getRoleName());
        return R.ok(sysRolesService.list(wrapper));
    }

    /**
     * 新增
     * @param params 新增参数
     * @return R
     */
    @PostMapping
    @Operation(summary = "新增" , description = "新增" )
    public R<Boolean> save(@RequestBody @Valid SaveRolesParams params) {
        SysRoles sysRoles = new SysRoles();
        BeanUtils.copyProperties(params,sysRoles);
        return R.ok(sysRolesService.save(sysRoles));
    }


    /**
     * 通过id更新
     * @param params 修改参数
     * @return R
     */
    @PutMapping("/{id}")
    @Operation(summary = "通过id更新" , description = "通过id更新" )
    public R<Boolean> updateById(@PathVariable("id") Long id,@RequestBody @Valid UpdateRolesParams params) {
        SysRoles sysRoles = new SysRoles();
        BeanUtils.copyProperties(params,sysRoles);
        sysRoles.setId(id);
        return R.ok(sysRolesService.updateById(sysRoles));
    }

    /**
     * 批量删除
     * @return R
     */
    @DeleteMapping
    @Operation(summary = "批量删除" , description = "批量删除" )
    public R<Boolean> removeBatchByIds(@RequestParam("ids") String ids) {
        return R.ok(sysRolesService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    /**
     * 通过id删除
     * @param id 删除参数
     * @return R
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "通过id删除" , description = "通过id删除" )
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(sysRolesService.removeById(id));
    }

    /**
     * 通过id查询
     * @param id 查询参数
     * @return R
     */
    @GetMapping("/{id}" )
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    public R<SysRoles> getById(@PathVariable("id") Long id) {
        return R.ok(sysRolesService.getById(id));
    }


    /**
     * 更新角色菜单
     * @param params 修改参数
     * @return R
     */
    @PutMapping("/menus")
    @Operation(summary = "更新角色菜单" , description = "更新角色菜单" )
    public R<Boolean> updateRoleMenus(@RequestBody @Valid UpdateRoleMenusParams params) {
        return R.ok(sysRolesService.updateRoleMenus(params));
    }

    /**
     * 通过id查询角色菜单ID
     * @param id 查询参数
     * @return R
     */
    @GetMapping("/menus/{id}")
    @Operation(summary = "通过id查询角色菜单ID" , description = "通过id查询角色菜单ID" )
    public R<GetRoleMenusVO> getRoleMenus(@PathVariable("id") Long id) {
        return R.ok(sysRolesService.getRoleMenus(id));
    }
}
