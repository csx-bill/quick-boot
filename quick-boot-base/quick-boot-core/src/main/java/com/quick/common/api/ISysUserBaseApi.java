package com.quick.common.api;

import com.quick.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ISysUserBaseApi {
    Result<List<String>> getUserRole(@RequestParam(value = "userId") String userId);

    Result<List<String>> getUserRolePermission(@RequestParam(value = "roleId") String roleId);
}
