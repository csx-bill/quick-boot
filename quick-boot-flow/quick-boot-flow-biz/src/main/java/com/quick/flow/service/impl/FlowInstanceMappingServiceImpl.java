package com.quick.flow.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowDefinition;
import com.quick.flow.entity.FlowInstanceMapping;
import com.quick.flow.entity.FlowNodeInstance;
import com.quick.flow.mapper.FlowInstanceMappingMapper;
import com.quick.flow.service.IFlowInstanceMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class FlowInstanceMappingServiceImpl extends ServiceImpl<FlowInstanceMappingMapper, FlowInstanceMapping> implements IFlowInstanceMappingService {

    @Override
    public List<FlowInstanceMapping> selectFlowInstanceMappingList(String flowInstanceId, String nodeInstanceId) {
        return list(new LambdaQueryWrapper<FlowInstanceMapping>()
                .eq(FlowInstanceMapping::getFlowInstanceId, flowInstanceId)
                .eq(FlowInstanceMapping::getNodeInstanceId,nodeInstanceId)
        );
    }

    @Override
    public FlowInstanceMapping selectFlowInstanceMapping(String flowInstanceId, String nodeInstanceId) {
        return getOne(new LambdaQueryWrapper<FlowInstanceMapping>()
                .eq(FlowInstanceMapping::getFlowInstanceId, flowInstanceId)
                .eq(FlowInstanceMapping::getNodeInstanceId,nodeInstanceId)
        );
    }

    @Override
    public boolean updateStatus(String flowInstanceId, String nodeInstanceId,Integer status) {
        FlowInstanceMapping flowInstanceMapping = new FlowInstanceMapping();
        flowInstanceMapping.setStatus(status);
        return update(flowInstanceMapping,new LambdaUpdateWrapper<FlowInstanceMapping>()
                .eq(FlowInstanceMapping::getFlowInstanceId,flowInstanceId)
                .eq(FlowInstanceMapping::getNodeInstanceId,nodeInstanceId)
        );
    }
}
