package com.quick.online.controller;

import apijson.JSON;
import apijson.JSONResponse;
import apijson.RequestMethod;
import apijson.StringUtil;
import apijson.orm.Parser;
import apijson.router.APIJSONRouterController;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.vo.Result;
import com.quick.online.parser.OnlineFunctionParser;
import com.quick.online.parser.OnlineParser;
import com.quick.online.parser.OnlineVerifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.Map;

import static apijson.framework.APIJSONConstant.*;
import static apijson.framework.APIJSONConstant.REQUEST_;

@Slf4j
@RestController
@RequestMapping("/online/crud")
@RequiredArgsConstructor
@Tag(name = "增删改查统一接口")
public class OnlineApiController extends APIJSONRouterController<Long> {

    public static final String VERIFY = "verify";

    public static final String TYPE = "type";
    public static final String VALUE = "value";

    @Override
    public Parser<Long> newParser(HttpSession session, RequestMethod method) {
        return super.newParser(session, method).setNeedVerify(false).setNeedVerifyLogin(false)
                .setNeedVerifyContent(true);
    }


    @Operation(summary = "增删改查统一接口", description = "增删改查统一接口，这个一个接口可替代 7 个万能通用接口")
    @PostMapping("/{method}/{tag}")
    @Override
    public String router(@PathVariable("method") String method, @PathVariable("tag") String tag,@RequestParam(value = "params",required = false) Map<String, String> params,@RequestBody String request, HttpSession session) {
        return super.router(method, tag, params, request, session);
    }

    @SneakyThrows
    @Operation(summary = "重新加载 APIJSON配置", description = "重新加载 APIJSON配置")
    @PostMapping("reload")
    @Override
    public JSONObject reload(@RequestBody String request) {
        JSONObject requestObject = null;
        String type;
        JSONObject value;
        try {
            requestObject = OnlineParser.parseRequest(request);
            type = requestObject.getString(TYPE);
            value = requestObject.getJSONObject(VALUE);
        } catch (Exception e) {
            return JSON.parseObject(Result.fail(e.getMessage()));
        }


        JSONObject result = OnlineParser.newSuccessResult();

        boolean reloadAll = StringUtil.isEmpty(type, true) || "ALL".equals(type);

        if (reloadAll || "ACCESS".equals(type)) {
            try {
                result.put(ACCESS_, OnlineVerifier.initAccess(false, null, value));
            } catch (ServerException e) {
                e.printStackTrace();
                result.put(ACCESS_, OnlineParser.newErrorResult(e));
            }
        }

        if (reloadAll || "FUNCTION".equals(type)) {
            try {
                result.put(FUNCTION_, OnlineFunctionParser.init(false, null, value));
            } catch (ServerException e) {
                e.printStackTrace();
                result.put(FUNCTION_, OnlineParser.newErrorResult(e));
            }
        }

        if (reloadAll || "REQUEST".equals(type)) {
            try {
                result.put(REQUEST_, OnlineVerifier.initRequest(false, null, value));
            } catch (ServerException e) {
                e.printStackTrace();
                result.put(REQUEST_, OnlineParser.newErrorResult(e));
            }
        }

        if (reloadAll || "DOCUMENT".equals(type)) {
            try {
                result.put(DOCUMENT_, OnlineVerifier.initDocument(false, null, value));
            } catch (ServerException e) {
                e.printStackTrace();
                result.put(DOCUMENT_, OnlineParser.newErrorResult(e));
            }
        }

        return JSON.parseObject(Result.success(result));
    }
}