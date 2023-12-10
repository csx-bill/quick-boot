package com.quick.system.service;

import com.quick.system.entity.SysDataRule;

import java.util.List;

public interface ISysDataRuleApiService {

    List<SysDataRule> queryDataRuleByPath(String menuPath,String apiPath);


}
