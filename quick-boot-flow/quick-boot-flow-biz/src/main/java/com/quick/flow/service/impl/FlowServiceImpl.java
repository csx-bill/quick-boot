package com.quick.flow.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.flow.engine.common.FlowDeploymentStatus;
import com.quick.flow.engine.dao.FlowDefinitionDAO;
import com.quick.flow.engine.dao.FlowDeploymentDAO;
import com.quick.flow.engine.engine.ProcessEngine;
import com.quick.flow.engine.entity.FlowDefinition;
import com.quick.flow.engine.entity.FlowDeployment;
import com.quick.flow.engine.param.CreateFlowParam;
import com.quick.flow.engine.param.DeployFlowParam;
import com.quick.flow.engine.param.GetFlowModuleParam;
import com.quick.flow.engine.param.UpdateFlowParam;
import com.quick.flow.engine.result.CreateFlowResult;
import com.quick.flow.engine.result.DeployFlowResult;
import com.quick.flow.engine.result.FlowModuleResult;
import com.quick.flow.engine.result.UpdateFlowResult;
import com.quick.flow.enums.FlowModuleStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Author: james zhangxiao
 * @Date: 4/11/22
 * @Description: 流程处理类
 */
@Service
public class FlowServiceImpl {

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private FlowDefinitionDAO flowDefinitionDAO;

    @Resource
    private FlowDeploymentDAO flowDeploymentDAO;

    /**
     * 创建流程
     *
     * @param createFlowParam 创建流程基础信息
     * @return 创建流程结果
     */
    public CreateFlowResult createFlow(CreateFlowParam createFlowParam) {
        return processEngine.createFlow(createFlowParam);
    }

    /**
     * 保存流程模型数据
     *
     * @param updateFlowParam 保存流程模型请求参数
     * @return 保存流程模型结果
     */
    public UpdateFlowResult updateFlow(UpdateFlowParam updateFlowParam) {
        return processEngine.updateFlow(updateFlowParam);
    }

    /**
     * 发布流程
     *
     * @param deployFlowParam 发布流程请求参数
     * @return 发布流程结果
     */
    public DeployFlowResult deployFlow(DeployFlowParam deployFlowParam) {
        return processEngine.deployFlow(deployFlowParam);
    }

    /**
     * 获取单个流程
     *
     * @param getFlowModuleParam 查询单个流程参数
     * @return 查询单个流程结果
     */
    public FlowModuleResult getFlowModule(GetFlowModuleParam getFlowModuleParam) {
        return processEngine.getFlowModule(getFlowModuleParam);
    }

    public IPage<FlowDefinition> page(Page<FlowDefinition> page, Wrapper<FlowDefinition> queryWrapper){
        IPage<FlowDefinition> pageRes = flowDefinitionDAO.page(page, queryWrapper);
        for (FlowDefinition flowDefinition : pageRes.getRecords()) {
            long count = flowDeploymentDAO.count(new LambdaQueryWrapper<FlowDeployment>()
                    .eq(FlowDeployment::getFlowModuleId,flowDefinition.getFlowModuleId())
                    .eq(FlowDeployment::getStatus,FlowDeploymentStatus.DEPLOYED)
            );
            if (count >= 1) {
                //4 已发布
                flowDefinition.setStatus(FlowModuleStatusEnum.PUBLISHED.getValue());
            }
        }
        return pageRes;
    }

}
