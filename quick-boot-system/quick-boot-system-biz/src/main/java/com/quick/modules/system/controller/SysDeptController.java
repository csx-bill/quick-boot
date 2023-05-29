package com.quick.modules.system.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.modules.system.entity.SysDept;
import com.quick.modules.system.service.ISysDeptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/SysDept")
@RequiredArgsConstructor
@Tag(name = "部门信息")
@PreAuth(replace = "SysDept:")
public class SysDeptController extends SuperController<ISysDeptService, SysDept, String> {

}
