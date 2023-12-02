package com.quick.system.service;

import com.quick.system.entity.SysDictData;

import java.util.List;

public interface ISysDictApiService {
    /**
     * 通过字典code获取字典数据 Text
     *
     * @param code
     * @param key
     * @return
     */
    String translateDict(String code, String key);

    List<SysDictData> queryDictDataByDictCode(String dictCode);
}
