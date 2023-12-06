package com.quick.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.constant.CacheConstant;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.system.entity.SysRole;
import com.quick.system.entity.SysUser;
import com.quick.system.entity.SysUserRole;
import com.quick.system.req.AuthorizedUserPageParam;
import com.quick.system.service.ISysRoleService;
import com.quick.system.service.ISysUserRoleService;
import com.quick.system.service.ISysUserService;
import com.quick.system.vo.RolePermissionsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Tag(name = "角色信息")
@PreAuth(replace = "system:sys_role:")
public class SysRoleController extends SuperController<ISysRoleService, SysRole,String> {

    private final ISysUserService sysUserService;
    private final ISysUserRoleService sysUserRoleService;


    @GetMapping(value = "/getRolePermissions")
    @Operation(summary = "查询角色权限", description = "查询角色权限")
    public Result<RolePermissionsVO> getRolePermissions(String id) {
        return Result.success(baseService.getRolePermissions(id));
    }

    @PostMapping(value = "/saveRolePermissions")
    @Operation(summary = "保存角色权限", description = "保存角色权限")
    @CacheEvict(value={CacheConstant.SYS_ROLE_PERMISSION_CACHE}, allEntries=true)
    public Result<Boolean> saveRolePermissions(@RequestBody RolePermissionsVO vo) {
        return Result.success(baseService.saveRolePermissions(vo));
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
    @CacheEvict(value={CacheConstant.SYS_USER_ROLE_CACHE}, allEntries=true)
    public Result<Boolean> cancelAuthorizedUser(String roleId,String userId) {
        return Result.success(sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId,roleId)
                .eq(SysUserRole::getUserId,userId)
                )
        );
    }

    @DeleteMapping(value = "/batchCancelAuthorizedUser")
    @Operation(summary = "批量取消已授权用户", description = "批量取消已授权用户")
    @CacheEvict(value={CacheConstant.SYS_USER_ROLE_CACHE}, allEntries=true)
    public Result<Boolean> batchCancelAuthorizedUser(String roleId,String userIds) {
        return Result.success(sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId,roleId)
                .in(SysUserRole::getUserId,Arrays.asList(userIds.split(",")))
                )
        );
    }

    @PostMapping(value = "/batchAuthorizedUser")
    @Operation(summary = "批量授权用户", description = "批量授权用户")
    public Result<Boolean> batchAuthorizedUser(String roleId,String userIds) {
        return Result.success(sysUserRoleService.batchAuthorizedUser(roleId,userIds));
    }
}
