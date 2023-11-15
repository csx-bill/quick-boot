package com.quick.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 对象转换工具类
 *
 * @Author Lizhou
 */
@SuppressWarnings("ALL")
public class ObjConvertUtils {
    /**
     * 获取类的所有属性，包括父类
     *
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    public static boolean isNotEmpty(Object object) {
        if (object != null && !"".equals(object) && !object.equals("null")) {
            return (true);
        }
        return (false);

    }
}