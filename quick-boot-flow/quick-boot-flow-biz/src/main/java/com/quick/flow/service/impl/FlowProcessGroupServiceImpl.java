package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowProcessGroup;
import com.quick.flow.mapper.FlowProcessGroupMapper;
import com.quick.flow.service.IFlowProcessGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowProcessGroupServiceImpl extends ServiceImpl<FlowProcessGroupMapper, FlowProcessGroup> implements IFlowProcessGroupService {

}
