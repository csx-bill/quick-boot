package com.quick.modules.online.entity;

import com.quick.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "页面配置信息")
public class SysAccessSchema extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "表权限ID")
    private Long accessId;

    @Schema(description = "页面配置")
    private String schema;
}
