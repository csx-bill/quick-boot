package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowInstanceData;
import com.quick.flow.mapper.FlowInstanceDataMapper;
import com.quick.flow.service.IFlowInstanceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowInstanceDataServiceImpl extends ServiceImpl<FlowInstanceDataMapper, FlowInstanceData> implements IFlowInstanceDataService {

    @Override
    public FlowInstanceData selectByFlowInstanceIdAndInstanceDataId(String flowInstanceId, String instanceDataId) {
        return getOne(new LambdaQueryWrapper<FlowInstanceData>()
                .eq(FlowInstanceData::getFlowInstanceId, flowInstanceId)
                .eq(FlowInstanceData::getInstanceDataId,instanceDataId)
        );
    }

    @Override
    public FlowInstanceData selectByflowInstanceIdRecentOne(String flowInstanceId) {
        return baseMapper.selectByflowInstanceIdRecentOne(flowInstanceId);
    }
}
