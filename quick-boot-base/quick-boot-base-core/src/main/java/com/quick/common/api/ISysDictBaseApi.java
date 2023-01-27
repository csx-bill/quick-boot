package com.quick.common.api;

import com.quick.common.vo.Result;

public interface ISysDictBaseApi {
    Result<String> translateDict(String dictCode, String dictValue);
}
