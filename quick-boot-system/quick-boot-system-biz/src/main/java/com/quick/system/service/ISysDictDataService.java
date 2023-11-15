package com.quick.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.system.entity.SysDictData;

import java.util.List;


public interface ISysDictDataService extends IService<SysDictData> {
    List<SysDictData> queryDictDataByDictCode(String dictCode);

}
