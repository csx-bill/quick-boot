package com.quick.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.online.dto.*;
import com.quick.online.entity.Request;
import com.quick.online.service.IRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
@Tag(name = "请求参数校验配置")
@PreAuth(replace = "online:request:")
public class RequestController extends SuperController<IRequestService, String, Request, RequestPageQuery, RequestSaveDTO, RequestUpdateDTO> {

}
