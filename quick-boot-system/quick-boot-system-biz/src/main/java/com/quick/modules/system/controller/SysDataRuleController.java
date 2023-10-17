package com.quick.modules.system.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.modules.system.entity.SysDataRule;
import com.quick.modules.system.service.ISysDataRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/SysDataRule")
@RequiredArgsConstructor
@Tag(name = "数据权限")
@PreAuth(replace = "SysDataRule:")
public class SysDataRuleController extends SuperController<ISysDataRuleService, SysDataRule, String> {

}
