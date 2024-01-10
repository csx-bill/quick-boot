package com.quick.flow.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowInstance;
import com.quick.flow.mapper.FlowInstanceMapper;
import com.quick.flow.service.IFlowInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowInstanceServiceImpl extends ServiceImpl<FlowInstanceMapper, FlowInstance> implements IFlowInstanceService {

    @Override
    public FlowInstance selectByFlowInstanceId(String flowInstanceId) {
        return getOne(new LambdaQueryWrapper<FlowInstance>().eq(FlowInstance::getFlowInstanceId, flowInstanceId));
    }

    @Transactional
    @Override
    public boolean updateStatus(String flowInstanceId, Integer status) {
        return update(FlowInstance.builder().status(status).build(), new LambdaUpdateWrapper<FlowInstance>()
                .eq(FlowInstance::getFlowInstanceId,flowInstanceId));
    }
}
