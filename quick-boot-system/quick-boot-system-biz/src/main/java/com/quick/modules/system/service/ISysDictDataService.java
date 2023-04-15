package com.quick.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.system.entity.SysDictData;

import java.util.List;


public interface ISysDictDataService extends IService<SysDictData> {
    List<SysDictData> queryDictDataByDictCode(String dictCode);

}
