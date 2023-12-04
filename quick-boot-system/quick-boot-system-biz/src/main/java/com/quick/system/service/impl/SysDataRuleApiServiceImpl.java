package com.quick.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quick.common.constant.CommonConstant;
import com.quick.system.entity.SysDataRule;
import com.quick.system.mapper.SysDataRuleMapper;
import com.quick.system.service.ISysDataRuleApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysDataRuleApiServiceImpl  implements ISysDataRuleApiService {
    private final SysDataRuleMapper sysDataRuleMapper;

    /**
     * 根据接口URL查找数据权限规则
     * @param apiPath
     * @return
     */

    @Override
    public List<SysDataRule> queryDataRuleByApiPath(String apiPath) {
        return sysDataRuleMapper.selectList(new LambdaQueryWrapper<SysDataRule>()
                .eq(SysDataRule::getApiPath, apiPath)
                .eq(SysDataRule::getStatus, CommonConstant.A));
    }
}
