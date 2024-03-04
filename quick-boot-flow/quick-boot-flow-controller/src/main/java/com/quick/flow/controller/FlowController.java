package com.quick.flow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.PageParam;
import com.quick.common.vo.Result;
import com.quick.flow.dto.FlowDefinitionPageQuery;
import com.quick.flow.engine.entity.FlowDefinition;
import com.quick.flow.engine.param.CreateFlowParam;
import com.quick.flow.engine.param.DeployFlowParam;
import com.quick.flow.engine.param.GetFlowModuleParam;
import com.quick.flow.engine.param.UpdateFlowParam;
import com.quick.flow.engine.result.CreateFlowResult;
import com.quick.flow.engine.result.DeployFlowResult;
import com.quick.flow.engine.result.FlowModuleResult;
import com.quick.flow.engine.result.UpdateFlowResult;
import com.quick.flow.service.impl.FlowServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/flow")
@RequiredArgsConstructor
@Tag(name = "流程管理")
@PreAuth(replace = "flow:")
public class FlowController {

    @Resource
    private FlowServiceImpl logicFlowService;

    @PostMapping(value = "/createFlow")
    @Operation(summary = "创建流程")
    public Result<CreateFlowResult> createFlow(@RequestBody CreateFlowParam createFlowParam) {
        CreateFlowResult createFlowResult = logicFlowService.createFlow(createFlowParam);
        return  Result.success(createFlowResult);
    }

    @PostMapping(value = "/saveFlowModel")
    @Operation(summary = "保存流程模型数据")
    public Result<UpdateFlowResult> saveFlowModel(@RequestBody UpdateFlowParam updateFlowParam) {
        UpdateFlowResult updateFlowResult = logicFlowService.updateFlow(updateFlowParam);
        return Result.success(updateFlowResult);
    }

    @PostMapping(value = "/deployFlow")
    @Operation(summary = "发布流程")
    public Result<DeployFlowResult> deployFlow(@RequestBody DeployFlowParam deployFlowParam) {
        DeployFlowResult deployFlowResult = logicFlowService.deployFlow(deployFlowParam);
        return Result.success(deployFlowResult);
    }

    @PostMapping(value = "/queryFlow")
    @Operation(summary = "查询单个流程数据")
    public Result<FlowModuleResult> queryFlow(@RequestBody GetFlowModuleParam getFlowModuleParam) {
        FlowModuleResult flowModuleResult = logicFlowService.getFlowModule(getFlowModuleParam);
        return Result.success(flowModuleResult);
    }

    @PostMapping(value = "/page")
    @Operation(summary = CommonConstant.PAGE_MSG)
    public Result<IPage<FlowDefinition>> page(@RequestBody PageParam<FlowDefinitionPageQuery> pageParam) {
        Page<FlowDefinition> page = pageParam.buildPage();
        FlowDefinition entity = BeanUtil.toBean(pageParam.getModel(), FlowDefinition.class);
        LambdaQueryWrapper<FlowDefinition> queryWrapper = new LambdaQueryWrapper(entity);
        return Result.success(logicFlowService.page(page, queryWrapper));
    }
}
