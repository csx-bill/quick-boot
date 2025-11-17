package com.quik.boot.modules.system.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quik.boot.modules.common.vo.R;
import com.quik.boot.modules.common.vo.UserInfo;
import com.quik.boot.modules.system.entity.SysUsers;
import com.quik.boot.modules.system.req.ProjectsUsersPageParams;
import com.quik.boot.modules.system.req.SaveProjectUserParams;
import com.quik.boot.modules.system.req.UpdateProjectUserParams;
import com.quik.boot.modules.system.req.UsersListParams;
import com.quik.boot.modules.system.service.SysProjectsUsersService;
import com.quik.boot.modules.system.service.SysUsersService;
import com.quik.boot.modules.system.vo.ProjectsUsersDetailsVO;
import com.quik.boot.modules.system.vo.ProjectsUsersVO;
import com.quik.boot.modules.system.vo.SysUsersVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(description = "users" , name = "用户管理" )
public class SysUsersController {
    private final SysUsersService sysUsersService;

    private final SysProjectsUsersService sysProjectsUsersService;

    /**
     * 获取当前用户全部信息
     * @return 用户信息
     */
    @GetMapping("/info")
    public R<UserInfo> info() {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        SysUsers sysUser = sysUsersService.lambdaQuery().eq(SysUsers::getId, tokenInfo.loginId).one();
        if(Objects.isNull(sysUser)){
            R.failed();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(sysUser.getId());
        userInfo.setUsername(sysUser.getUsername());
        userInfo.setAvatar(sysUser.getAvatar());
        userInfo.setAccessToken(tokenInfo.getTokenValue());
        return R.ok(userInfo);
    }


    /**
     * 列表查询
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/list")
    @Operation(summary = "列表查询" , description = "列表查询" )
    public R<List<SysUsersVO>> list(@ParameterObject @Valid UsersListParams params) {
        return R.ok(sysUsersService.usersList(params));
    }

    /**
     * 查询未加入当前项目的用户列表
     * @param projectId 项目ID
     * @return R
     */
    @GetMapping("/not-in-project")
    @Operation(summary = "查询未加入当前项目的用户列表" , description = "查询未加入当前项目的用户列表" )
    public R<List<SysUsers>> notInProject(@RequestParam("projectId")Long projectId) {
        return R.ok(sysUsersService.findNotInProjectUsers(projectId));
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询" , description = "分页查询" )
    public R<IPage<ProjectsUsersVO>> projectsUsersPage(@ParameterObject Page page, @ParameterObject ProjectsUsersPageParams params) {
        return R.ok(sysProjectsUsersService.projectsUsersPage(page,params));
    }

    /**
     * 新增
     * @param params 新增参数
     * @return R
     */
    @PostMapping
    @Operation(summary = "新增" , description = "新增" )
    public R<Boolean> saveProjectUser(@RequestBody SaveProjectUserParams params) {
        return R.ok(sysProjectsUsersService.saveProjectUser(params));
    }


    /**
     * 通过id更新
     * @param params 修改参数
     * @return R
     */
    @PutMapping("/{id}")
    @Operation(summary = "通过id更新" , description = "通过id更新" )
    public R<Boolean> updateById(@PathVariable("id") Long id,@RequestBody UpdateProjectUserParams params) {
        return R.ok(sysProjectsUsersService.updateProjectUser(id,params));
    }


    /**
     * 批量删除
     * @return R
     */
    @DeleteMapping
    @Operation(summary = "批量删除" , description = "批量删除" )
    public R<Boolean> removeBatchByIds(@RequestParam("ids") String ids) {
        return R.ok(sysProjectsUsersService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    /**
     * 删除项目用户
     * @param id
     * @return R
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目用户" , description = "删除项目用户" )
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(sysProjectsUsersService.removeById(id));
    }

    /**
     * 通过id查询
     * @param id 查询参数
     * @return R
     */
    @GetMapping("/{id}" )
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    public R<ProjectsUsersDetailsVO> getProjectUser(@PathVariable("id") Long id) {
        return R.ok(sysProjectsUsersService.getProjectUser(id));
    }

}
