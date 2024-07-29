package com.quick.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.online.dto.*;
import com.quick.online.entity.OnlCgformHead;
import com.quick.online.service.IOnlCgformHeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/online/onlCgformHead")
@RequiredArgsConstructor
@Tag(name = "表单管理")
@PreAuth(replace = "online:onl_cgform_head:")
public class OnlCgformHeadController extends SuperController<IOnlCgformHeadService, Long, OnlCgformHead, OnlCgformHeadPageQuery, OnlCgformHeadSaveDTO, OnlCgformHeadUpdateDTO> {

    @GetMapping(value = "/getOnlCgformHeadDetails")
    @Operation(summary = "根据ID查询表单详情(含附表)")
    @Parameter(name = "id",required = true)
    public Result<OnlCgformHeadDetailsVO> getOnlCgformHeadDetails(@RequestParam("id") Long id) {
        return Result.success(baseService.getOnlCgformHeadDetails(id));
    }

    @GetMapping(value = "/details")
    @Operation(summary = "根据ID查询表单详情(不含附表)")
    @Parameter(name = "id",required = true)
    public Result<OnlCgformHeadVO> details(@RequestParam("id") Long id) {
        return Result.success(baseService.details(id));
    }

    @Operation(summary = "同步数据库表信息", description = "同步数据库表信息")
    @PostMapping(value = "/syncedTableInfo")
    public Result<Boolean> syncedTableInfo(@RequestBody SyncedTableInfoDTO syncedTableInfoDTO) throws IOException {
        return Result.success(baseService.syncedTableInfo(syncedTableInfoDTO));
    }

//    @PreAuth("{}"+ CommonConstant.ADD)
//    @PostMapping(value = "/bizSave")
//    @Operation(summary = CommonConstant.SAVE_MSG)
//    public Result<OnlCgformHeadVO> bizSave(@RequestBody OnlCgformHeadVO saveDTO) {
//        baseService.save(entity);
//        return Result.success(entity);
//    }

}
