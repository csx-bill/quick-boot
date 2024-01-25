package com.quick.system.controller;


import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.system.dto.SysDictPageQuery;
import com.quick.system.dto.SysDictSaveDTO;
import com.quick.system.dto.SysDictUpdateDTO;
import com.quick.system.entity.SysDict;
import com.quick.system.service.ISysDictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
@Tag(name = "字典信息")
@PreAuth(replace = "system:sys_dict:")
public class SysDictController extends SuperController<ISysDictService, Long, SysDict, SysDictPageQuery, SysDictSaveDTO, SysDictUpdateDTO> {

}
