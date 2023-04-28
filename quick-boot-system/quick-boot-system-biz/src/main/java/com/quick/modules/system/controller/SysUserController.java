package com.quick.modules.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysUser;
import com.quick.modules.system.req.SysUserPageParam;
import com.quick.modules.system.service.ISysUserService;
import com.quick.modules.system.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/SysUser")
@RequiredArgsConstructor
@Tag(name = "用户信息")
public class SysUserController {
    private final ISysUserService sysUserService;


    @GetMapping(value = "/getUserInfo")
    @Operation(summary = "获取当前用户信息", description = "获取当前用户信息")
    public Result<UserInfoVO> getUserInfo() {
        String userId = StpUtil.getLoginId().toString();
        SysUser sysUser = sysUserService.getById(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(sysUser,userInfoVO);
        userInfoVO.setUserId(userId);
        return Result.success(userInfoVO);
    }

    @PostMapping(value = "/page")
    @Operation(summary = "分页查询用户", description = "分页查询用户")
    public Result<IPage<SysUser>> page(@RequestBody SysUserPageParam sysUserPageParam) {
        Page page = sysUserPageParam.buildPage();
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserPageParam,sysUser);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>(sysUser);
        return Result.success(sysUserService.page(page,queryWrapper));
    }

    @PostMapping(value = "/save")
    @Operation(summary = "保存用户", description = "保存用户")
    public Result<Boolean> save(@RequestBody SysUser sysUser) {
        return Result.success(sysUserService.save(sysUser));
    }

    @GetMapping(value = "/getById")
    @Operation(summary = "根据ID获取用户", description = "根据ID获取用户")
    public Result<SysUser> getById(String id) {
        SysUser sysUser = sysUserService.getById(id);
        sysUser.setPassword(null);
        return Result.success(sysUser);
    }

    @SaCheckPermission("SysUser:update")
    @PutMapping(value = "/updateById")
    @Operation(summary = "根据ID更新用户", description = "根据ID更新用户")
    public Result<Boolean> updateById(@RequestBody SysUser sysUser) {
        return Result.success(sysUserService.updateById(sysUser));
    }

    @DeleteMapping(value = "/removeById")
    @Operation(summary = "根据ID删除用户", description = "根据ID删除用户")
    public Result<Boolean> removeById(String id) {
        return Result.success(sysUserService.removeById(id));
    }

    @DeleteMapping(value = "/removeBatchByIds")
    @Operation(summary = "根据ID批量删除用户", description = "根据ID批量删除用户")
    public Result<Boolean> removeBatchByIds(String ids) {
        return Result.success(sysUserService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }
}
