package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowProcess;
import com.quick.flow.mapper.FlowProcessMapper;
import com.quick.flow.service.IFlowProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowProcessServiceImpl extends ServiceImpl<FlowProcessMapper, FlowProcess> implements IFlowProcessService {

}
