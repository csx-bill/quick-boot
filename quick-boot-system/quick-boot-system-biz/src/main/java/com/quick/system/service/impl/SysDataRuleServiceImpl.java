package com.quick.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.system.entity.SysDataRule;
import com.quick.system.mapper.SysDataRuleMapper;
import com.quick.system.service.ISysDataRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysDataRuleServiceImpl extends ServiceImpl<SysDataRuleMapper, SysDataRule> implements ISysDataRuleService {

}
