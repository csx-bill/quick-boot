package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowDefinition;
import com.quick.flow.mapper.FlowDefinitionMapper;
import com.quick.flow.service.IFlowDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowDefinitionServiceImpl extends ServiceImpl<FlowDefinitionMapper, FlowDefinition> implements IFlowDefinitionService {

    @Override
    public FlowDefinition selectByFlowModuleId(String flowModuleId) {
        return getOne(new LambdaQueryWrapper<FlowDefinition>().eq(FlowDefinition::getFlowModuleId, flowModuleId));
    }

    @Transactional
    @Override
    public boolean updateByFlowModuleId(FlowDefinition flowDefinition) {
        return update(flowDefinition,new LambdaUpdateWrapper<FlowDefinition>()
                .eq(FlowDefinition::getFlowModuleId,flowDefinition.getFlowModuleId()));
    }
}
