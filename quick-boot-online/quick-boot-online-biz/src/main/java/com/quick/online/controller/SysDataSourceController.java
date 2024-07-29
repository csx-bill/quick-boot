package com.quick.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.online.dto.SysDataSourcePageQuery;
import com.quick.online.dto.SysDataSourceSaveDTO;
import com.quick.online.dto.SysDataSourceUpdateDTO;
import com.quick.online.entity.SysDataSource;
import com.quick.online.service.ISysDataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/online/dataSource")
@RequiredArgsConstructor
@Tag(name = "数据源管理")
@PreAuth(replace = "online:sys_data_source:")
public class SysDataSourceController extends SuperController<ISysDataSourceService, Long, SysDataSource, SysDataSourcePageQuery, SysDataSourceSaveDTO, SysDataSourceUpdateDTO> {

    @GetMapping(value = "/list")
    @Operation(summary = "列表")
    public Result<List<SysDataSource>> list() {
        return Result.success(baseService.list());
    }

}
