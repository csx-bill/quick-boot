package com.quick.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.system.dto.SysDeptPageQuery;
import com.quick.system.dto.SysDeptSaveDTO;
import com.quick.system.dto.SysDeptUpdateDTO;
import com.quick.system.entity.SysDept;
import com.quick.system.service.ISysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
@Tag(name = "部门信息")
@PreAuth(replace = "system:sys_dept:")
public class SysDeptController extends SuperController<ISysDeptService, Long, SysDept, SysDeptPageQuery, SysDeptSaveDTO, SysDeptUpdateDTO> {

    @GetMapping(value = "/getDeptTree")
    @Operation(summary = "查询部门树", description = "查询部门树")
    public Result<List<SysDept>> getDeptTree() {
        return Result.success(baseService.getDeptTree());
    }

    @PostMapping(value = "/getDeptTreeSearch")
    @Operation(summary = "条件获取部门树", description = "条件获取部门树")
    public Result<List<SysDept>> getDeptTreeSearch(@RequestBody SysDept entity) {
        return Result.success(baseService.getSysDeptTree(
                baseService.list(new LambdaQueryWrapper(entity))
        ));
    }

}
