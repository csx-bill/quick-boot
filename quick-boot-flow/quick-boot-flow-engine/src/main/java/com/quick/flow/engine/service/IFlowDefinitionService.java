package com.quick.flow.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.flow.engine.entity.FlowDefinition;

public interface IFlowDefinitionService extends IService<FlowDefinition> {
    boolean updateByModuleId(FlowDefinition flowDefinition);
    FlowDefinition selectByModuleId(String flowModuleId);
}
