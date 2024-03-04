package com.quick.flow.engine.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.quick.flow.engine.mapper.FlowDefinitionMapper;
import com.quick.flow.engine.entity.FlowDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class FlowDefinitionDAO extends BaseDAO<FlowDefinitionMapper, FlowDefinition> {

    /**
     * UpdateByModuleId: update flowDefinitionPO by flowModuleId, return -1 while updateByModuleId failed.
     *
     * @param flowDefinition
     * @return int
     */
    public int updateByModuleId(FlowDefinition flowDefinition) {
        if (null == flowDefinition) {
            LOGGER.warn("updateByModuleId failed: flowDefinitionPO is null.");
            return -1;
        }
        try {
            String flowModuleId = flowDefinition.getFlowModuleId();
            if (StringUtils.isBlank(flowModuleId)) {
                LOGGER.warn("updateByModuleId failed: flowModuleId is empty.||flowDefinitionPO={}", flowDefinition);
                return -1;
            }
            UpdateWrapper<FlowDefinition> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("flow_module_id", flowModuleId);
            return baseMapper.update(flowDefinition, updateWrapper);
        } catch (Exception e) {
            LOGGER.error("update exception.||flowDefinitionPO={}", flowDefinition, e);
        }
        return -1;
    }

    /**
     * SelectByModuleId: query flowDefinitionPO by flowModuleId, return null while flowDefinitionPO can't be found.
     *
     * @param flowModuleId
     * @return flowDefinitionPO
     */
    public FlowDefinition selectByModuleId(String flowModuleId) {
        if (StringUtils.isBlank(flowModuleId)) {
            LOGGER.warn("getById failed: flowModuleId is empty.");
            return null;
        }
        try {
            return baseMapper.selectOne(new LambdaQueryWrapper<FlowDefinition>()
                    .eq(FlowDefinition::getFlowModuleId,flowModuleId));
        } catch (Exception e) {
            LOGGER.error("getById exception.||flowModuleId={}", flowModuleId, e);
        }
        return null;
    }
}
