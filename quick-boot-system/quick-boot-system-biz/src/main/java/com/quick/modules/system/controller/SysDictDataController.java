package com.quick.modules.system.controller;


import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysDictData;
import com.quick.modules.system.service.ISysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/SysDictData")
@RequiredArgsConstructor
@Tag(name = "字典数据信息")
@PreAuth(replace = "SysDictData:")
public class SysDictDataController extends SuperController<ISysDictDataService, SysDictData, String> {
    @GetMapping(value = "/queryDictDataByDictCode")
    @Operation(summary = "通过字典code获取字典数据", description = "通过字典code获取字典数据")
    public Result<List<SysDictData>> queryDictDataByDictCode(@RequestParam("dictCode") String dictCode) {
        return Result.success(baseService.queryDictDataByDictCode(dictCode));
    }

}
