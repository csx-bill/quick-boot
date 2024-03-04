package com.quick.flow.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.flow.engine.entity.FlowDeployment;

public interface IFlowDeploymentService extends IService<FlowDeployment> {
    FlowDeployment selectByDeployId(String flowDeployId);

    FlowDeployment selectRecentByFlowModuleId(String flowModuleId);
}
