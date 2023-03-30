package com.quick.modules.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysDict;
import com.quick.modules.system.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SysDict")
@RequiredArgsConstructor
@Tag(name = "字典信息")
public class SysDictController {
    private final ISysDictService sysDictService;


    @GetMapping(value = "/page")
    @Operation(summary = "分页查询字典", description = "分页查询字典")
    public Result<IPage<SysDict>> page(Page<SysDict> page) {
        return Result.success(sysDictService.page(page));
    }
}
