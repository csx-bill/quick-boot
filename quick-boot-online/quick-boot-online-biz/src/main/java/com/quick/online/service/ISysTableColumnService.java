package com.quick.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.online.entity.SysTableColumn;

import java.util.List;


public interface ISysTableColumnService extends IService<SysTableColumn> {
    List<SysTableColumn> selectByTableName(String tableName);

}
