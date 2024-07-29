package com.quick.online.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.online.dto.AccessVO;
import com.quick.online.entity.Access;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IAccessService extends IService<Access> {
    List<Map<String,String>> listByTableName(List<String> tableNames,String tableName);

    boolean sync(List<String> tableNames) throws IOException;

    AccessVO getAccessColumnsById(String id);
    boolean updateAccessColumnsById(AccessVO entity) throws IOException;
    boolean refactoringCRUDById(String id);

    JSONObject getAccessSchemaById(String id);

    boolean reload(String request);
}
