package com.quick.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.flow.entity.FlowDefinition;

public interface IFlowDefinitionService extends IService<FlowDefinition> {

    FlowDefinition selectByFlowModuleId(String flowModuleId);
    boolean updateByFlowModuleId(FlowDefinition flowDefinition);

}
