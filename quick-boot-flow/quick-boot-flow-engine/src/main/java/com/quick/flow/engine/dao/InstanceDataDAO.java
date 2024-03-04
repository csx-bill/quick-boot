package com.quick.flow.engine.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.flow.engine.mapper.FlowInstanceDataMapper;
import com.quick.flow.engine.entity.FlowInstanceData;
import org.springframework.stereotype.Repository;

@Repository
public class InstanceDataDAO extends BaseDAO<FlowInstanceDataMapper, FlowInstanceData> {

    public FlowInstanceData select(String flowInstanceId, String instanceDataId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<FlowInstanceData>()
                .eq(FlowInstanceData::getInstanceDataId,instanceDataId));
    }

    /**
     * select recent InstanceData order by id desc
     *
     * @param flowInstanceId
     * @return
     */
    public FlowInstanceData selectRecentOne(String flowInstanceId) {
        return baseMapper.selectRecentOne(flowInstanceId);
    }

    /**
     * insert instanceDataPO
     *
     * @param flowInstanceData
     * @return -1 while insert failed
     */
    public int insert(FlowInstanceData flowInstanceData) {
        try {
            return baseMapper.insert(flowInstanceData);
        } catch (Exception e) {
            // TODO: 2020/2/1 clear reentrant exception log 
            LOGGER.error("insert exception.||instanceDataPO={}", flowInstanceData, e);
        }
        return -1;
    }
}
