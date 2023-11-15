package com.quick.online.controller;

import apijson.JSON;
import apijson.JSONResponse;
import apijson.RequestMethod;
import apijson.framework.APIJSONController;
import apijson.framework.APIJSONParser;
import apijson.orm.Parser;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.vo.Result;
import com.quick.online.parser.OnlineJSONParse;
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
        Parser parser = new OnlineJSONParse();
        if (parser instanceof APIJSONParser) {
            ((APIJSONParser)parser).setSession(session);
        }
        parser.setMethod(method);
        parser.setNeedVerify(false);
        // 校验 Request 表的 structure
        parser.setNeedVerifyContent(true);
        return parser;
    }

    /**
     * 增删改查统一接口，这个一个接口可替代 7 个万能通用接口，牺牲一些路由解析性能来提升一点开发效率
     *
     * @param method
     * @param request
     * @param session
     * @return
     */
    @Operation(summary = "在线通用接口", description = "增删改查统一接口，这个一个接口可替代 7 个万能通用接口")
    @PostMapping(value = "{method}")
    @Override
    public String crud(@PathVariable String method, @RequestBody String request, HttpSession session) {
        return processResponse(super.crud(method, request, session));
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
    @Operation(summary = "在线通用接口", description = "增删改查统一接口，这个一个接口可替代 7 个万能通用接口")
    @Parameter(name = "method", description = "get,gets,head,heads,post,put,delete")
    @Parameter(name = "tag", description = "由后端Request表中指定")
    @Parameter(name = "params", description = "参数")
    @Override
    public String crudByTag(@PathVariable String method, @PathVariable String tag, @RequestParam Map<String, String> params, @RequestBody String request, HttpSession session) {
        return processResponse(super.crudByTag(method, tag, params, request, session));
    }

    /**
     * 统一响应 格式
     *
     * @param res
     * @return
     */
    private String processResponse(String res) {
        JSONObject result = JSON.parseObject(res);
        if (result.getIntValue(JSONResponse.KEY_CODE) == JSONResponse.CODE_SUCCESS) {
            return JSON.toJSONString(Result.success(result));
        } else {
            return JSON.toJSONString(Result.fail(result.getIntValue(JSONResponse.KEY_CODE), result.getString(JSONResponse.KEY_MSG)));
        }
    }
}