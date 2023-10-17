package com.quick.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.system.entity.SysDataRule;
import com.quick.modules.system.mapper.SysDataRuleMapper;
import com.quick.modules.system.service.ISysDataRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysDataRuleServiceImpl extends ServiceImpl<SysDataRuleMapper, SysDataRule> implements ISysDataRuleService {

}
