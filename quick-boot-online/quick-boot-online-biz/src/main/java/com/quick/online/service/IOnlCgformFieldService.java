package com.quick.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.online.entity.OnlCgformField;

import java.util.List;

public interface IOnlCgformFieldService extends IService<OnlCgformField> {
    List<OnlCgformField> selectByTableName(String tableName,String dsName);
}
