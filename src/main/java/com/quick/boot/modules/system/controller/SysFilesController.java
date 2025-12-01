package com.quick.boot.modules.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quick.boot.modules.common.project.ProjectContextHolder;
import com.quick.boot.modules.common.vo.ApiPage;
import com.quick.boot.modules.common.vo.R;
import com.quick.boot.modules.system.entity.SysFiles;
import com.quick.boot.modules.system.req.FilesPageParams;
import com.quick.boot.modules.system.service.SysFilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@AllArgsConstructor
@RequestMapping("/files")
@Tag(description = "files" , name = "文件管理" )
public class SysFilesController {

    private final SysFilesService sysFilesService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param params 查询参数
     * @return R
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询" , description = "分页查询" )
    public R<IPage<SysFiles>> page(@ParameterObject ApiPage page, @ParameterObject @Valid FilesPageParams params) {
        LambdaQueryWrapper<SysFiles> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysFiles::getProjectId, ProjectContextHolder.getProjectId());
        wrapper.eq(StringUtils.hasText(params.getType()), SysFiles::getType,params.getType());
        wrapper.like(StringUtils.hasText(params.getOriginal()), SysFiles::getFileName,params.getOriginal());
        return R.ok(sysFilesService.page(page,wrapper));
    }

    /**
     * 批量删除
     * @return R
     */
    @DeleteMapping
    @Operation(summary = "批量删除" , description = "批量删除" )
    public R<Boolean> removeBatchByIds(@RequestParam("ids") String ids) {
        return R.ok(sysFilesService.removeBatchByIds(Arrays.asList(ids.split(","))));
    }

    /**
     * 通过id删除
     * @param id 删除参数
     * @return R
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "通过id删除" , description = "通过id删除" )
    public R<Boolean> removeById(@PathVariable("id") Long id) {
        return R.ok(sysFilesService.removeById(id));
    }

    /**
     * 上传文件
     * @param file 资源
     */
    @PostMapping(value = "/upload")
    public R upload(@RequestPart("file") MultipartFile file,
                    @RequestParam(value = "type", required = false) String type) {
        return sysFilesService.uploadFile(file, type);
    }

    /**
     * 获取文件
     * @param fileName 文件名称
     * @param response
     * @return
     */
    @SaIgnore
    @GetMapping("/oss/file")
    public void file(String fileName, HttpServletResponse response) {
        sysFilesService.getFile(fileName, response);
    }

}
