package com.quick.system.api.fallback;

import com.quick.common.vo.Result;
import com.quick.system.api.ISysMenuApi;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SysMenuApiFallback implements ISysMenuApi {
    @Setter
    private Throwable cause;

    @Override
    public Result<List<String>> getPermission() {
        log.error("请求失败 {}", cause);
        return null;
    }
}
