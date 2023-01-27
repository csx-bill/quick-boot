package com.quick.system.api;

import com.quick.common.api.ISysDictBaseApi;
import com.quick.common.vo.Result;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * 数据字典 api
 */
@HttpExchange
public interface ISysDictApi extends ISysDictBaseApi {
    /**
     * 通过字典code获取字典数据 Text
     *
     * @param dictCode
     * @param dictValue
     * @return
     */
    @GetExchange("/SysDict/Api/translateDict")
    @Override
    Result<String> translateDict(@RequestParam("dictCode") String dictCode, @RequestParam("dictValue") String dictValue);
}
