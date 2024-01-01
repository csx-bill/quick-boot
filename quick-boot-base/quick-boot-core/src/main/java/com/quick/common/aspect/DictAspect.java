package com.quick.common.aspect;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quick.common.api.ISysDictBaseApi;
import com.quick.common.aspect.annotation.Dict;
import com.quick.common.constant.CommonConstant;
import com.quick.common.util.ObjConvertUtils;
import com.quick.common.vo.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典切面
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Aspect
@Component
public class DictAspect {

    @Autowired
    private ObjectMapper objectMapper;
    @Lazy
    @Autowired
    private ISysDictBaseApi sysDictBaseApi;

    /**
     * 切点，切入 controller 包下面的所有方法
     */
    @Pointcut("execution(public * com.quick.*.controller.*.*(..)) " +
            "|| @annotation(com.quick.common.aspect.annotation.Dict)")
    public void dict() {

    }

    @Around("dict()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        this.parseDictText(result);
        return result;
    }

    private void parseDictText(Object result) {
        if (result instanceof Result) {
            List<JSONObject> items = new ArrayList<>();
            if (((Result) result).getData() instanceof IPage) {
                // 获取结果集
                List records = ((IPage) ((Result) result).getData()).getRecords();

                //判断是否含有字典注解,没有注解返回
                Boolean hasDict = checkHasDict(records);
                if (!hasDict) {
                    return;
                }

                for (Object record : records) {
                    String json = "{}";
                    try {
                        // 解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
                        json = objectMapper.writeValueAsString(record);
                    } catch (JsonProcessingException e) {
                        log.error("Json解析失败：" + e);
                    }
                    JSONObject item = JSONObject.parseObject(json);
                    // 解决继承实体字段无法翻译问题
                    for (Field field : ObjConvertUtils.getAllFields(record)) {
                        //解决继承实体字段无法翻译问题
                        // 如果该属性上面有@Dict注解，则进行翻译
                        if (field.getAnnotation(Dict.class) != null) {
                            // 拿到注解的dictDataSource属性的值
                            String dictCode = field.getAnnotation(Dict.class).dictCode();
                            // 拿到注解的dictText属性的值
                            String text = field.getAnnotation(Dict.class).dictText();
                            //获取当前带翻译的值
                            String dictValue = String.valueOf(item.get(field.getName()));

                            //翻译字典值对应的text值
                            String textValue = translateDictValue(dictCode, dictValue);
                            //如果给了文本名
                            if (!StringUtils.isBlank(text)) {
                                item.put(text, textValue);
                            } else {
                                // 走默认策略
                                item.put(field.getName() + CommonConstant.DICT_TEXT_SUFFIX, textValue);
                            }
                        }
                    }
                    items.add(item);
                }
                ((IPage) ((Result) result).getData()).setRecords(items);
            }
        }
    }

    /**
     * 翻译字典文本
     *
     * @param dictCode
     * @param dictValue
     * @return
     */
    private String translateDictValue(String dictCode, String dictValue) {
        Result<String> result = sysDictBaseApi.translateDict(dictCode, dictValue);
        return result.getData();
    }

    /**
     * 检测返回结果集中是否包含Dict注解
     *
     * @param records
     * @return
     */
    private Boolean checkHasDict(List<Object> records) {
        if (!CollectionUtils.isEmpty(records)) {
            for (Field field : ObjConvertUtils.getAllFields(records.get(0))) {
                if (ObjConvertUtils.isNotEmpty(field.getAnnotation(Dict.class))) {
                    return true;
                }
            }
        }
        return false;
    }
}
