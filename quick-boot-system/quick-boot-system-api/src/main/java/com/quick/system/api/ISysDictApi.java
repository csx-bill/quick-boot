package com.quick.system.api;

import com.quick.common.api.ISysDictBaseApi;
import com.quick.common.constant.CommonConstant;
import com.quick.common.vo.Result;
import com.quick.system.api.dto.SysDictDataApiDTO;
import com.quick.system.api.factory.SysDictApiFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 数据字典 api
 */
@Component
@FeignClient(contextId = "ISysDictApi", value = CommonConstant.SERVICE_SYSTEM, fallbackFactory = SysDictApiFactory.class)
public interface ISysDictApi extends ISysDictBaseApi {
    /**
     * 通过字典code获取字典数据 Text
     *
     * @param dictCode
     * @param dictValue
     * @return
     */
    @GetMapping("/SysDict/Api/translateDict")
    @Override
    Result<String> translateDict(@RequestParam("dictCode") String dictCode, @RequestParam("dictValue") String dictValue);

    /**
     * 通过字典code获取字典数据
     * @param dictCode
     * @return
     */
    @GetMapping(value = "/SysDict/Api/queryDictDataByDictCode")
    Result<List<SysDictDataApiDTO>> queryDictDataByDictCode(@RequestParam("dictCode") String dictCode);
}
