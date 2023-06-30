package com.quick.modules.online.controller;

import apijson.RequestMethod;
import apijson.framework.APIJSONController;
import apijson.orm.Parser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/Online/Api")
@RequiredArgsConstructor
@Tag(name = "在线接口")
public class OnlineApiController extends APIJSONController<Long> {

    @Override
    public Parser<Long> newParser(HttpSession session, RequestMethod method) {
        return super.newParser(session, method)
                .setNeedVerify(false)
                // 校验 Request 表的 structure
                .setNeedVerifyContent(true);
    }

    /**增删改查统一接口，这个一个接口可替代 7 个万能通用接口，牺牲一些路由解析性能来提升一点开发效率
     * @param method
     * @param request
     * @param session
     * @return
     */
    @Operation(summary = "万能通用接口", description = "增删改查统一接口，这个一个接口可替代 7 个万能通用接口")
    @PostMapping(value = "{method}")
    @Override
    public String crud(@PathVariable String method, @RequestBody String request, HttpSession session) {
        return super.crud(method, request, session);
    }

    /**
     * 增删改查统一接口，这个一个接口可替代 7 个万能通用接口，牺牲一些路由解析性能来提升一点开发效率
     *
     * @param method
     * @param tag
     * @param params
     * @param request
     * @param session
     * @return
     */
    @PostMapping("{method}/{tag}")
    @Operation(summary = "万能通用接口", description = "增删改查统一接口，这个一个接口可替代 7 个万能通用接口")
    @Parameter(name = "method", description = "get,gets,head,heads,post,put,delete")
    @Parameter(name = "tag", description = "由后端Request表中指定")
    @Parameter(name = "params", description = "参数")
    @Override
    public String crudByTag(@PathVariable String method, @PathVariable String tag, @RequestParam Map<String, String> params, @RequestBody String request, HttpSession session) {
        return super.crudByTag(method, tag, params, request, session);
    }

}