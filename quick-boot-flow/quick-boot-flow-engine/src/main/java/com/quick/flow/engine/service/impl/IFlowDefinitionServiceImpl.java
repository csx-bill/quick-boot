package com.quick.flow.engine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.quick.flow.engine.entity.FlowDefinition;
import com.quick.flow.engine.mapper.FlowDefinitionMapper;
import com.quick.flow.engine.service.IFlowDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IFlowDefinitionServiceImpl extends ServiceImpl<FlowDefinitionMapper, FlowDefinition> implements IFlowDefinitionService {

    @Override
    public boolean updateByModuleId(FlowDefinition flowDefinition) {
        return SqlHelper.retBool(this.getBaseMapper().update(flowDefinition, new LambdaUpdateWrapper<FlowDefinition>()
                .eq(FlowDefinition::getFlowModuleId,flowDefinition.getFlowModuleId())));
    }

    @Override
    public FlowDefinition selectByModuleId(String flowModuleId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<FlowDefinition>()
                .eq(FlowDefinition::getFlowModuleId,flowModuleId));
    }
}
