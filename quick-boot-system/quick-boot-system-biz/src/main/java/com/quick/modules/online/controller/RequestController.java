package com.quick.modules.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.modules.online.entity.Request;
import com.quick.modules.online.service.IRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/Request")
@RequiredArgsConstructor
@Tag(name = "请求参数校验配置")
@PreAuth(replace = "Request:")
public class RequestController extends SuperController<IRequestService, Request, String> {

}
