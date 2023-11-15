package com.quick.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.online.entity.SysAccessSchema;

public interface ISysAccessSchemaService extends IService<SysAccessSchema> {
    SysAccessSchema getSchema(String accessId);
}
