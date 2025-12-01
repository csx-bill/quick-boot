package com.quick.boot.modules.common.files.local;

import lombok.Data;
/**
 * 本地文件 配置信息
 * bucket 设置公共读权限
 */
@Data
public class LocalFilesProperties {

    /**
     * 是否开启
     */
    private boolean enable;

    /**
     * 默认路径
     */
    private String basePath;

}
