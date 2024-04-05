package com.quick.flow.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.quick.flow.dto.Flow;
import com.quick.flow.service.IExecuteService;
import com.warm.flow.core.dto.FlowParams;
import com.warm.flow.core.entity.Instance;
import com.warm.flow.core.service.InsService;
import com.warm.tools.utils.IdUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * @author minliuhua
 * @description: 流程执行serviceImpl
 * @date: 2023/5/29 13:09
 */
@Service
public class ExecuteServiceImpl implements IExecuteService {

    @Resource
    private InsService insService;


    @Override
    public void startFlow(Flow flow, String tableName) {
        Long id = IdUtils.nextId();
        FlowParams flowParams = FlowParams.build().flowCode(tableName)
                .createBy(StpUtil.getLoginIdAsString());
//                .nickName(
//                        //user.getUser().getNickName()
//                        //StpUtil.get
//                );
        Instance instance = insService.start(String.valueOf(id), flowParams);
        flow.setId(id);
        flow.setInstanceId(instance.getId());
        flow.setNodeCode(instance.getNodeCode());
        flow.setNodeName(instance.getNodeName());
        flow.setFlowStatus(instance.getFlowStatus());
    }
}
