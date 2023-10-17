package com.quick.modules.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.online.entity.Access;
import com.quick.modules.online.vo.AccessVO;

import java.util.List;
import java.util.Map;

public interface IAccessService extends IService<Access> {
    List<Map<String,String>> listByTableName(List<String> tableNames,String tableName);

    boolean sync(List<String> tableNames);

    AccessVO getAccessColumnsById(String id);
    boolean updateAccessColumnsById(AccessVO entity);
}
