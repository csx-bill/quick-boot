package com.quick.modules.system.api.controller;

import com.quick.common.vo.Result;
import com.quick.modules.system.service.ISysDictApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/SysDict/Api")
@RequiredArgsConstructor
@Tag(name = "数据字典API")
public class SysDictApiController {
    private final ISysDictApiService sysDictApiService;

    @GetMapping(value = "/translateDict")
    @Operation(summary = "通过字典code获取字典数据 Text", description = "通过字典code获取字典数据 Text")
    public Result<String> translateDict(@RequestParam("dictCode") String dictCode, @RequestParam("dictValue") String dictValue) {
        return Result.success(sysDictApiService.translateDict(dictCode, dictValue));
    }
}
