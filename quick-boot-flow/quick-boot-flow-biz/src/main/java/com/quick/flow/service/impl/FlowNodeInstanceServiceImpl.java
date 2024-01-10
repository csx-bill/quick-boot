package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.common.entity.BaseEntity;
import com.quick.flow.entity.FlowNodeInstance;
import com.quick.flow.mapper.FlowNodeInstanceMapper;
import com.quick.flow.service.IFlowNodeInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowNodeInstanceServiceImpl extends ServiceImpl<FlowNodeInstanceMapper, FlowNodeInstance> implements IFlowNodeInstanceService {

    @Override
    public List<FlowNodeInstance> selectDescByFlowInstanceId(String flowInstanceId) {
        return list(new LambdaQueryWrapper<FlowNodeInstance>()
                .eq(FlowNodeInstance::getFlowInstanceId, flowInstanceId)
                .orderByDesc(BaseEntity::getId));
    }

    @Override
    public List<FlowNodeInstance> selectByFlowInstanceId(String flowInstanceId) {
        return list(new LambdaQueryWrapper<FlowNodeInstance>()
                .eq(FlowNodeInstance::getFlowInstanceId, flowInstanceId));
    }

    @Override
    public FlowNodeInstance selectByNodeInstanceId(String nodeInstanceId) {
        return getOne(new LambdaQueryWrapper<FlowNodeInstance>()
                .eq(FlowNodeInstance::getNodeInstanceId, nodeInstanceId));
    }

    @Override
    public FlowNodeInstance selectBySourceInstanceId(String flowInstanceId, String sourceNodeInstanceId, String nodeKey) {
        return getOne(new LambdaQueryWrapper<FlowNodeInstance>()
                .eq(FlowNodeInstance::getFlowInstanceId, flowInstanceId)
                .eq(FlowNodeInstance::getSourceNodeInstanceId,sourceNodeInstanceId)
                .eq(FlowNodeInstance::getNodeKey,nodeKey)
        );
    }

    @Override
    public FlowNodeInstance selectRecentOne(String flowInstanceId) {
        List<FlowNodeInstance> flowNodeInstances = selectDescByFlowInstanceId(flowInstanceId);
        return CollectionUtils.isEmpty(flowNodeInstances) ? null : flowNodeInstances.get(0);
    }
}
