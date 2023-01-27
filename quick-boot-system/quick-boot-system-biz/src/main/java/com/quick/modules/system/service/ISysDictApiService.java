package com.quick.modules.system.service;

public interface ISysDictApiService {
    /**
     * 通过字典code获取字典数据 Text
     *
     * @param code
     * @param key
     * @return
     */
    String translateDict(String code, String key);
}
