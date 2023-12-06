package com.quick.system.api;

import com.quick.common.constant.CommonConstant;
import com.quick.common.vo.Result;
import com.quick.system.api.factory.SysMenuApiFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(contextId = "ISysMenuApi", value = CommonConstant.SERVICE_SYSTEM, fallbackFactory = SysMenuApiFactory.class)
public interface ISysMenuApi {

    @GetMapping("/menu/api/getPermission")
    Result<List<String>> getPermission();
}
