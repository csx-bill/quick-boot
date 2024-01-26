package com.quick.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.flow.entity.FlowProcessStarter;
import com.quick.flow.mapper.FlowProcessStarterMapper;
import com.quick.flow.service.IFlowProcessStarterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowProcessStarterServiceImpl extends ServiceImpl<FlowProcessStarterMapper, FlowProcessStarter> implements IFlowProcessStarterService {

}
