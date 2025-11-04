package com.quik.boot.modules.system.controller;


import com.quik.boot.modules.common.vo.R;
import com.quik.boot.modules.system.entity.SysMenus;
import com.quik.boot.modules.system.req.MenusTreeParams;
import com.quik.boot.modules.system.req.SaveMenusParams;
import com.quik.boot.modules.system.service.SysMenusService;
import com.quik.boot.modules.system.vo.SysMenusVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/menus")
@Tag(description = "menus" , name = "菜单管理" )
public class SysMenusController {
    private final SysMenusService sysMenusService;


    @GetMapping("/tree")
    @Operation(summary = "树形菜单" , description = "树形菜单" )
    public R<List<SysMenusVO>> getTree(@ParameterObject @Valid MenusTreeParams params) {
        return R.ok(sysMenusService.treeMenu(params));
    }

    /**
     * 新增
     * @param params 新增参数
     * @return R
     */
    @PostMapping
    @Operation(summary = "新增" , description = "新增" )
    public R<Boolean> save(@RequestBody @Valid SaveMenusParams params) {
        SysMenus sysMenus = new SysMenus();
        BeanUtils.copyProperties(params,sysMenus);
        return R.ok(sysMenusService.save(sysMenus));
    }


    /**
     * 通过id更新
     * @param params 修改参数
     * @return R
     */
    @PutMapping("/{id}")
    @Operation(summary = "通过id更新" , description = "通过id更新" )
    public R<Boolean> updateById(@PathVariable("id") Long id,@RequestBody SaveMenusParams params) {
        SysMenus sysMenus = new SysMenus();
        BeanUtils.copyProperties(params,sysMenus);
        sysMenus.setId(id);
        return R.ok(sysMenusService.updateById(sysMenus));
    }

    /**
     * 批量删除
     * @return R
     */
    @DeleteMapping
    @Operation(summary = "批量删除" , description = "批量删除" )
    public R<Boolean> removeBatchByIds(@RequestParam("ids") String ids) {
        return R.ok(sysMenusService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    /**
     * 通过id删除
     * @param id 删除参数
     * @return R
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "通过id删除" , description = "通过id删除" )
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(sysMenusService.removeById(id));
    }

    /**
     * 通过id查询
     * @param id 查询参数
     * @return R
     */
    @GetMapping("/{id}" )
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    public R<SysMenus> getById(@PathVariable("id") Long id) {
        return R.ok(sysMenusService.getById(id));
    }
}
