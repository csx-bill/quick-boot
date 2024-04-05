package com.quick.flow.service.impl;

import com.quick.flow.service.IHhDefService;
import com.warm.flow.core.service.DefService;
import com.warm.flow.core.service.NodeService;
import com.warm.flow.core.service.SkipService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * @author minliuhua
 * @description: 流程定义serviceImpl
 * @date: 2023/5/29 13:09
 */
@Service
public class HhDefServiceImpl implements IHhDefService {

    @Resource
    private DefService defService;

    @Resource
    private NodeService nodeService;

    @Resource
    private SkipService skipService;

}
