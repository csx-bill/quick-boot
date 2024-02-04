package com.quick.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.constant.CommonConstant;
import com.quick.common.controller.SuperController;
import com.quick.common.util.PageParam;
import com.quick.common.vo.Result;
import com.quick.system.dto.*;
import com.quick.system.entity.SysTenant;
import com.quick.system.service.ISysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
@Tag(name = "租户信息")
@PreAuth(replace = "system:sys_tenant:")
public class SysTenantController extends SuperController<ISysTenantService, Long, SysTenant, SysTenantPageQuery, SysTenantSaveDTO, SysTenantUpdateDTO> {

    @GetMapping(value = "/list")
    @Operation(summary = "查询全部有效的租户")
    public Result<List<SysTenant>> list() {
        List<SysTenant> tenants = baseService.list(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getStatus,"0"))
                .stream()
                .filter(tenant -> tenant.getStartTime().isBefore(LocalDateTime.now()))
                .filter(tenant -> tenant.getEndTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        return Result.success(tenants);
    }

}
