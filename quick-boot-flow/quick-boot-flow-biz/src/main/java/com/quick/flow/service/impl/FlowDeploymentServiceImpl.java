package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowDeployment;
import com.quick.flow.mapper.FlowDeploymentMapper;
import com.quick.flow.service.IFlowDeploymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FlowDeploymentServiceImpl extends ServiceImpl<FlowDeploymentMapper, FlowDeployment> implements IFlowDeploymentService {

    @Override
    public FlowDeployment selectByFlowDeployId(String flowDeployId) {
        return getOne(new LambdaQueryWrapper<FlowDeployment>().eq(FlowDeployment::getFlowDeployId, flowDeployId));
    }

    @Override
    public FlowDeployment selectByFlowModuleId(String flowModuleId) {
        return getOne(new LambdaQueryWrapper<FlowDeployment>().eq(FlowDeployment::getFlowModuleId, flowModuleId));
    }
}
