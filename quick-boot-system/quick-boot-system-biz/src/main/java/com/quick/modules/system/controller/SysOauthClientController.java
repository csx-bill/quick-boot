package com.quick.modules.system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SysOauthClient")
@RequiredArgsConstructor
@Tag(name = "客户端信息")
public class SysOauthClientController {

}
