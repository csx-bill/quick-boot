package com.quick.online.controller;

import apijson.JSON;
import apijson.JSONResponse;
import apijson.RequestMethod;
import apijson.orm.Parser;
import apijson.router.APIJSONRouterController;
import com.alibaba.fastjson.JSONObject;
import com.quick.common.vo.Result;
import com.quick.online.util.ApijsonInitUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/crud")
@RequiredArgsConstructor
@Tag(name = "增删改查统一接口")
public class OnlineApiController extends APIJSONRouterController<String> {

    @Override
    public Parser<String> newParser(HttpSession session, RequestMethod method) {
        return super.newParser(session, method).setNeedVerify(false).setNeedVerifyLogin(false)
                .setNeedVerifyContent(true);
    }


    @Operation(summary = "增删改查统一接口", description = "增删改查统一接口，这个一个接口可替代 7 个万能通用接口")
    @PostMapping("/{method}/{tag}")
    @Override
    public String router(@PathVariable String method, @PathVariable String tag,@RequestParam Map<String, String> params,@RequestBody String request, HttpSession session) {
        return processResponse(super.router(method, tag, params, request, session));
    }

    @SneakyThrows
    @Operation(summary = "重新加载 APIJSON配置", description = "重新加载 APIJSON配置")
    @PostMapping("reload")
    @Override
    public JSONObject reload(String type) {
        ApijsonInitUtil.init();
        return JSON.parseObject(Result.success());
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