package com.quick.common.api;

import com.quick.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ISysUserBaseApi {
    Result<List<Long>> getUserRole(@RequestParam(value = "userId") Long userId);

    Result<List<String>> getUserRolePermission(@RequestParam(value = "roleId") Long roleId);
}
