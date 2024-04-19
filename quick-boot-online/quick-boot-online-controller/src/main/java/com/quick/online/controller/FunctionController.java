package com.quick.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.online.dto.*;
import com.quick.online.entity.Function;
import com.quick.online.service.IFunctionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/online/function")
@RequiredArgsConstructor
@Tag(name = "远程函数")
@PreAuth(replace = "online:function:")
public class FunctionController extends SuperController<IFunctionService, Long, Function, FunctionPageQuery, FunctionSaveDTO, FunctionUpdateDTO> {

}
