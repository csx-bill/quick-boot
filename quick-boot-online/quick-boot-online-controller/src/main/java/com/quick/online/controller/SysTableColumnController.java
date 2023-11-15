package com.quick.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.online.entity.SysTableColumn;
import com.quick.online.service.ISysTableColumnService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SysTableColumn")
@RequiredArgsConstructor
@Tag(name = "表字段配置")
@PreAuth(replace = "SysTableColumn:")
public class SysTableColumnController extends SuperController<ISysTableColumnService, SysTableColumn, String> {
}
