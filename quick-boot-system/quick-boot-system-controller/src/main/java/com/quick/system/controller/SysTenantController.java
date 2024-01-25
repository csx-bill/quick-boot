package com.quick.system.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.system.dto.*;
import com.quick.system.entity.SysTenant;
import com.quick.system.service.ISysTenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
@Tag(name = "租户信息")
@PreAuth(replace = "system:sys_tenant:")
public class SysTenantController extends SuperController<ISysTenantService, Long, SysTenant, SysTenantPageQuery, SysTenantSaveDTO, SysTenantUpdateDTO> {

}
