package com.quick.flow.engine.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.flow.engine.mapper.FlowDeploymentMapper;
import com.quick.flow.engine.entity.FlowDeployment;
import org.springframework.stereotype.Repository;

@Repository
public class FlowDeploymentDAO extends BaseDAO<FlowDeploymentMapper, FlowDeployment> {


    public FlowDeployment selectByDeployId(String flowDeployId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<FlowDeployment>()
                .eq(FlowDeployment::getFlowDeployId,flowDeployId));
    }

    /**
     * SelectRecentByFlowModuleId : query recent flowDeploymentPO by flowModuleId.
     *
     * @param  flowModuleId
     * @return flowDeploymentPO
     */
    public FlowDeployment selectRecentByFlowModuleId(String flowModuleId) {
        return baseMapper.selectByModuleId(flowModuleId);
    }
}
