package com.quick.modules.online.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.online.entity.SysAccessSchema;

public interface ISysAccessSchemaService extends IService<SysAccessSchema> {
    SysAccessSchema getSchema(String accessId);
}
