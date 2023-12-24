package com.quick.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.online.dto.AccessVO;
import com.quick.online.entity.Access;

import java.util.List;
import java.util.Map;

public interface IAccessService extends IService<Access> {
    List<Map<String,String>> listByTableName(List<String> tableNames,String tableName);

    boolean sync(List<String> tableNames);

    AccessVO getAccessColumnsById(String id);
    boolean updateAccessColumnsById(AccessVO entity);
    boolean refactoringCRUDById(String id);
}
