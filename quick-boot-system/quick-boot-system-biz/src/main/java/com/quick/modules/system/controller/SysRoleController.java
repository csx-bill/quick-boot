package com.quick.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysRole;
import com.quick.modules.system.entity.SysUser;
import com.quick.modules.system.entity.SysUserRole;
import com.quick.modules.system.req.AuthorizedUserPageParam;
import com.quick.modules.system.req.SysRolePageParam;
import com.quick.modules.system.service.ISysRoleService;
import com.quick.modules.system.service.ISysUserRoleService;
import com.quick.modules.system.service.ISysUserService;
import com.quick.modules.system.vo.RolePermissionsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;


@Slf4j
@RestController
@RequestMapping("/SysRole")
@RequiredArgsConstructor
@Tag(name = "角色信息")
public class SysRoleController {

    private final ISysRoleService sysRoleService;
    private final ISysUserService sysUserService;
    private final ISysUserRoleService sysUserRoleService;

    @PostMapping(value = "/page")
    @Operation(summary = "分页查询角色", description = "分页查询角色")
    public Result<IPage<SysRole>> page(@RequestBody SysRolePageParam sysRolePageParam) {
        Page page = sysRolePageParam.buildPage();
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRolePageParam,sysRole);
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>(sysRole);
        return Result.success(sysRoleService.page(page,queryWrapper));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存角色", description = "保存角色")
    public Result<Boolean> save(@RequestBody SysRole sysRole) {
        return Result.success(sysRoleService.save(sysRole));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取角色", description = "根据ID获取角色")
    public Result<SysRole> getById(String id) {
        return Result.success(sysRoleService.getById(id));
    }

    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新角色", description = "根据ID更新角色")
    public Result<Boolean> updateById(@RequestBody SysRole sysRole) {
        return Result.success(sysRoleService.updateById(sysRole));
    }

    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除角色", description = "根据ID删除角色")
    public Result<Boolean> removeById(String id) {
        return Result.success(sysRoleService.removeById(id));
    }

    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除角色", description = "根据ID批量删除角色")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(sysRoleService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    @GetMapping(value = "/getRolePermissions")
    @Operation(summary = "查询角色权限", description = "查询角色权限")
    public Result<RolePermissionsVO> getRolePermissions(String id) {
        return Result.success(sysRoleService.getRolePermissions(id));
    }

    @PostMapping(value = "/saveRolePermissions")
    @Operation(summary = "保存角色权限", description = "保存角色权限")
    public Result<Boolean> saveRolePermissions(@RequestBody RolePermissionsVO vo) {
        return Result.success(sysRoleService.saveRolePermissions(vo));
    }

    @PostMapping(value = "/authorizedUserPage")
    @Operation(summary = "分页查询已授权用户", description = "分页查询已授权用户")
    public Result<IPage<SysUser>> authorizedUserPage(@RequestBody AuthorizedUserPageParam pageParam) {
        Page page = pageParam.buildPage();
        return Result.success(sysUserService.authorizedUserPage(page,pageParam));
    }

    @PostMapping(value = "/unauthorizedUserPage")
    @Operation(summary = "分页查询未授权用户", description = "分页查询未授权用户")
    public Result<IPage<SysRole>> unauthorizedUserPage(@RequestBody AuthorizedUserPageParam pageParam) {
        Page page = pageParam.buildPage();
        return Result.success(sysUserService.unauthorizedUserPage(page,pageParam));
    }

    @DeleteMapping(value = "/cancelAuthorizedUser")
    @Operation(summary = "取消已授权用户", description = "取消已授权用户")
    public Result<Boolean> cancelAuthorizedUser(String roleId,String userId) {
        return Result.success(sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId,roleId)
                .eq(SysUserRole::getUserId,userId)
                )
        );
    }

    @DeleteMapping(value = "/batchCancelAuthorizedUser")
    @Operation(summary = "批量取消已授权用户", description = "批量取消已授权用户")
    public Result<Boolean> batchCancelAuthorizedUser(String roleId,String userIds) {
        return Result.success(sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId,roleId)
                .in(SysUserRole::getUserId,Arrays.asList(userIds.split(",")))
                )
        );
    }
}
