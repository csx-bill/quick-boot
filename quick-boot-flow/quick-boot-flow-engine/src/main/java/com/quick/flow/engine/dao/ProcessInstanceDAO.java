package com.quick.flow.engine.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.flow.engine.mapper.FlowInstanceMapper;
import com.quick.flow.engine.entity.FlowInstance;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ProcessInstanceDAO extends BaseDAO<FlowInstanceMapper, FlowInstance> {

    public FlowInstance selectByFlowInstanceId(String flowInstanceId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<FlowInstance>()
                .eq(FlowInstance::getFlowInstanceId,flowInstanceId));
    }

    /**
     * insert flowInstancePO
     *
     * @param flowInstance
     * @return -1 while insert failed
     */
    public int insert(FlowInstance flowInstance) {
        try {
            return baseMapper.insert(flowInstance);
        } catch (Exception e) {
            // TODO: 2020/2/1 clear reentrant exception log
            LOGGER.error("insert exception.||flowInstancePO={}", flowInstance, e);
        }
        return -1;
    }

    public void updateStatus(String flowInstanceId, int status) {
        FlowInstance flowInstance = selectByFlowInstanceId(flowInstanceId);
        updateStatus(flowInstance, status);
    }

    public void updateStatus(FlowInstance flowInstance, int status) {
        flowInstance.setStatus(status);
        flowInstance.setUpdateTime(LocalDateTime.now());
        baseMapper.updateStatus(flowInstance);
    }
}
