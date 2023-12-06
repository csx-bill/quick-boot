package com.quick.system.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.system.entity.SysDept;
import com.quick.system.service.ISysDeptService;
import com.quick.system.vo.SysDeptTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
@Tag(name = "部门信息")
@PreAuth(replace = "system:sys_dept:")
public class SysDeptController extends SuperController<ISysDeptService, SysDept, String> {

    @GetMapping(value = "/getDeptTree")
    @Operation(summary = "查询部门树", description = "查询部门树")
    public Result<List<SysDeptTreeVO>> getDeptTree() {
        return Result.success(baseService.getDeptTree());
    }

}
