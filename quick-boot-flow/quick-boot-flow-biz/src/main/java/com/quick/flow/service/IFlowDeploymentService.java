package com.quick.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.flow.entity.FlowDeployment;

public interface IFlowDeploymentService extends IService<FlowDeployment> {
    FlowDeployment selectByFlowDeployId(String flowDeployId);

    FlowDeployment selectByFlowModuleId(String flowModuleId);


}
