package com.quick.flow.engine.dao;

import com.quick.flow.engine.mapper.FlowInstanceMappingMapper;
import com.quick.flow.engine.entity.FlowInstanceMapping;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlowInstanceMappingDAO extends BaseDAO<FlowInstanceMappingMapper, FlowInstanceMapping> {

    /**
     * Used for multiple instances scene
     *
     * @param flowInstanceId
     * @param nodeInstanceId
     * @return
     */
    public List<FlowInstanceMapping> selectFlowInstanceMappingPOList(String flowInstanceId, String nodeInstanceId) {
        return baseMapper.selectFlowInstanceMappingList(flowInstanceId, nodeInstanceId);
    }

    /**
     * Used for single instances scene
     *
     * @param flowInstanceId
     * @param nodeInstanceId
     * @return
     */
    public FlowInstanceMapping selectFlowInstanceMappingPO(String flowInstanceId, String nodeInstanceId) {
        return baseMapper.selectFlowInstanceMapping(flowInstanceId, nodeInstanceId);
    }

    /**
     * Insert: insert flowInstanceMappingPO, return -1 while insert failed.
     *
     * @param  flowInstanceMapping
     * @return
     */
    public int insert(FlowInstanceMapping flowInstanceMapping) {
        try {
            return baseMapper.insert(flowInstanceMapping);
        } catch (Exception e) {
            LOGGER.error("insert exception.||flowInstanceMappingPO={}", flowInstanceMapping, e);
        }
        return -1;
    }

    public void updateType(String flowInstanceId, String nodeInstanceId, int type) {
        FlowInstanceMapping flowInstanceMapping = new FlowInstanceMapping();
        flowInstanceMapping.setFlowInstanceId(flowInstanceId);
        flowInstanceMapping.setNodeInstanceId(nodeInstanceId);
        flowInstanceMapping.setType(type);
        flowInstanceMapping.setUpdateTime(LocalDateTime.now());
        baseMapper.updateType(flowInstanceMapping);
    }
}
