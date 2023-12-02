package com.quick.system.api.fallback;

import com.quick.common.vo.Result;
import com.quick.system.api.ISysDictApi;
import com.quick.system.api.dto.SysDictDataApiDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SysDictApiFallback implements ISysDictApi {
    @Setter
    private Throwable cause;

    @Override
    public Result<String> translateDict(String dictCode, String dictValue) {
        log.error("请求失败 {}", cause);
        return null;
    }

    @Override
    public Result<List<SysDictDataApiDTO>> queryDictDataByDictCode(String dictCode) {
        log.error("请求失败 {}", cause);
        return null;
    }
}
