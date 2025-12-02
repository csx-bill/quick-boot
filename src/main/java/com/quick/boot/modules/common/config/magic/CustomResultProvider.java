package com.quick.boot.modules.common.config.magic;

import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.context.RequestEntity;
import org.ssssssss.magicapi.core.interceptor.ResultProvider;
import org.ssssssss.magicapi.modules.db.model.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端是百度AMIS，按其响应格式要求转换
 */
@Component
public class CustomResultProvider implements ResultProvider {
    /**
    *   定义返回结果，默认返回JsonBean
    */
	@Override
	public Object buildResult(RequestEntity requestEntity, int code, String message, Object data) {
        Object finalData = data==null?new HashMap<>():data;
        // 如果对分页格式有要求的话，可以对data的类型进行判断，进而返回不同的格式
		return new HashMap<String,Object>(){
			{
				put("status", code);
				put("msg", message);
				put("data", finalData);
			}
		};
	}

	/**
	 *   定义分页返回结果，该项会被封装在Json结果内，
	 *   此方法可以不覆盖，默认返回PageResult
	 */
	@Override
	public Object buildPageResult(RequestEntity requestEntity, Page page, long total, List<Map<String, Object>> data) {
        List<Map<String, Object>> finalData = data==null? new ArrayList<>():data;
        return new HashMap<String,Object>(){
			{
                put("total", total);
				put("items", finalData);
			}
		};
	}
}