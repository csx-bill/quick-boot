package com.quick.online.controller;

import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.constant.CommonConstant;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.online.dto.GenTemplateDetailsVO;
import com.quick.online.dto.GenTemplatePageQuery;
import com.quick.online.dto.GenTemplateSaveDTO;
import com.quick.online.dto.GenTemplateUpdateDTO;
import com.quick.online.entity.GenTemplate;
import com.quick.online.service.IGenTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/online/genTemplate")
@RequiredArgsConstructor
@Tag(name = "模板管理")
@PreAuth(replace = "online:gen_template:")
public class GenTemplateController extends SuperController<IGenTemplateService, Long, GenTemplate, GenTemplatePageQuery, GenTemplateSaveDTO, GenTemplateUpdateDTO> {


    @PreAuth("{}"+ CommonConstant.ADD)
    @PostMapping(value = "/saveGenTemplate")
    @Operation(summary = CommonConstant.SAVE_MSG)
    public Result<Boolean> saveGenTemplate(@RequestBody GenTemplateSaveDTO saveDTO) {
        return Result.success(baseService.saveGenTemplate(saveDTO));
    }

    @GetMapping(value = "/getGenTemplateById")
    @Operation(summary = "根据ID查询模板详情")
    @Parameter(name = "id",required = true)
    public Result<GenTemplateDetailsVO> getGenTemplateById(@RequestParam("id") Long id) {
        return Result.success(baseService.getGenTemplateById(id));
    }

    @PreAuth("{}"+ CommonConstant.UPDATE)
    @PutMapping(value = "/updateGenTemplateById")
    @Operation(summary = CommonConstant.UPDATE_BY_ID_MSG)
    public Result<Boolean> updateGenTemplateById(@RequestBody GenTemplateUpdateDTO updateDTO) {
        return Result.success(baseService.updateGenTemplateById(updateDTO));
    }

}
