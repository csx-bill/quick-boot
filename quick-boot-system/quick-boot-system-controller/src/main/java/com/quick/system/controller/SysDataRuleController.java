package com.quick.system.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.system.dto.SysDataRulePageQuery;
import com.quick.system.dto.SysDataRuleSaveDTO;
import com.quick.system.dto.SysDataRuleUpdateDTO;
import com.quick.system.service.ISysDataRuleService;
import com.quick.system.entity.SysDataRule;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/dataRule")
@RequiredArgsConstructor
@Tag(name = "数据权限")
@PreAuth(replace = "system:sys_data_rule:")
public class SysDataRuleController extends SuperController<ISysDataRuleService, String, SysDataRule, SysDataRulePageQuery, SysDataRuleSaveDTO, SysDataRuleUpdateDTO> {

}
