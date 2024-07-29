package com.quick.online.controller;

import apijson.JSON;
import apijson.RequestMethod;
import apijson.framework.APIJSONController;
import apijson.orm.Parser;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.vo.Result;
import com.quick.online.service.IAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/online")
@RequiredArgsConstructor
@Tag(name = "增删改查统一接口")
public class OnlineApiController extends APIJSONController<Object> {

    private final IAccessService accessService;

    @Override
    public Parser<Object> newParser(HttpSession session, RequestMethod method) {
        return super.newParser(session, method).setNeedVerify(false).setNeedVerifyLogin(false)
                .setNeedVerifyContent(true);
    }

    /**
     * 增删改查统一接口，这个一个接口可替代 7 个万能通用接口，牺牲一些路由解析性能来提升一点开发效率
     *
     * @param method
     * @param request
     * @param session
     * @return
     */
    @Operation(summary = "增删改查统一接口", description = "增删改查统一接口，这个一个接口可替代 7 个万能通用接口")
    @PostMapping(value = "/crud/{method}")
    @Override
    public String crud(@PathVariable(name="method") String method, @RequestBody String request, HttpSession session) {
        return super.crud(method, request, session);
    }

    @SneakyThrows
    @Operation(summary = "重新加载 APIJSON配置", description = "重新加载 APIJSON配置")
    @PostMapping("reload")
    @Override
    public JSONObject reload(@RequestBody String request) {
        accessService.reload(request);
        return JSON.parseObject(Result.success());
    }
}