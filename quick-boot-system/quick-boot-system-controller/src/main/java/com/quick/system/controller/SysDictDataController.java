package com.quick.system.controller;


import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.system.dto.SysDictDataPageQuery;
import com.quick.system.dto.SysDictDataSaveDTO;
import com.quick.system.dto.SysDictDataUpdateDTO;
import com.quick.system.entity.SysDictData;
import com.quick.system.service.ISysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/dictData")
@RequiredArgsConstructor
@Tag(name = "字典数据信息")
//@PreAuth(replace = "system:sys_dict_data:")
@PreAuth(replace = "system:sys_dict:")
public class SysDictDataController extends SuperController<ISysDictDataService, String, SysDictData, SysDictDataPageQuery, SysDictDataSaveDTO, SysDictDataUpdateDTO> {
    @GetMapping(value = "/queryDictDataByDictCode")
    @Operation(summary = "通过字典code获取字典数据", description = "通过字典code获取字典数据")
    public Result<List<SysDictData>> queryDictDataByDictCode(@RequestParam("dictCode") String dictCode) {
        return Result.success(baseService.queryDictDataByDictCode(dictCode));
    }

}
