package com.quick.flow.handler;

import com.warm.flow.core.entity.RootEntity;
import com.warm.flow.core.handler.DataFillHandler;
import com.warm.flow.core.utils.IdUtils;
import com.warm.flow.core.utils.ObjectUtil;

import java.util.Date;
import java.util.Objects;

/**
 * 填充器
 *
 * @author warm
 */
public class FlowDataFillHandler implements DataFillHandler {
    @Override
    public void idFill(Object object) {
        RootEntity entity = (RootEntity) object;
        if (ObjectUtil.isNotNull(entity)) {
            if (Objects.isNull(entity.getId())) {
                entity.setId(IdUtils.nextId());
            }
        }
    }

    @Override
    public void insertFill(Object object) {
        RootEntity entity = (RootEntity) object;
        if (ObjectUtil.isNotNull(entity)) {
            Date date = ObjectUtil.isNotNull(entity.getCreateTime())
                    ? entity.getCreateTime() : new Date();
            entity.setCreateTime(date);
            entity.setUpdateTime(date);
        }
    }

    @Override
    public void updateFill(Object object) {
        RootEntity entity = (RootEntity) object;
        if (ObjectUtil.isNotNull(entity)) {
            entity.setUpdateTime(new Date());
        }
    }
}
