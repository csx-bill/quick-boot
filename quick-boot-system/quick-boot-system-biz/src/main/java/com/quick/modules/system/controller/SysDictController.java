package com.quick.modules.system.controller;


import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.modules.system.entity.SysDict;
import com.quick.modules.system.service.ISysDictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/SysDict")
@RequiredArgsConstructor
@Tag(name = "字典信息")
@PreAuth(replace = "SysDict:")
public class SysDictController extends SuperController<ISysDictService, SysDict, String> {

}
