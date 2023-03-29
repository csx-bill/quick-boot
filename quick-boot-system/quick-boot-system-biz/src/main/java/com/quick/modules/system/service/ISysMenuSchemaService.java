package com.quick.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quick.modules.system.entity.SysMenuSchema;

public interface ISysMenuSchemaService extends IService<SysMenuSchema> {
    SysMenuSchema queryByPath(String path);
}
