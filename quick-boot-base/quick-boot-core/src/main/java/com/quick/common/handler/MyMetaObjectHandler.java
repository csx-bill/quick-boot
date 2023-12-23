package com.quick.common.handler;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.quick.common.constant.CommonConstant;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 填充器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CommonConstant.CREATETIME, () -> LocalDateTime.now(), LocalDateTime.class);
        //this.strictInsertFill(metaObject, CommonConstant.CREATEBY, () -> StpUtil.getLoginIdAsString(), String.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, CommonConstant.UPDATETIME, () -> LocalDateTime.now(), LocalDateTime.class);
        //this.strictUpdateFill(metaObject, CommonConstant.UPDATEBY, () -> StpUtil.getLoginIdAsString(), String.class);
    }
}
