package com.quick.flow.engine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.engine.entity.FlowDeployment;
import com.quick.flow.engine.mapper.FlowDeploymentMapper;
import com.quick.flow.engine.service.IFlowDeploymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowDeploymentServiceImpl extends ServiceImpl<FlowDeploymentMapper, FlowDeployment> implements IFlowDeploymentService {
    @Override
    public FlowDeployment selectByDeployId(String flowDeployId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<FlowDeployment>()
                .eq(FlowDeployment::getFlowDeployId,flowDeployId));
    }

    @Override
    public FlowDeployment selectRecentByFlowModuleId(String flowModuleId) {
        return baseMapper.selectByModuleId(flowModuleId);
    }
}
