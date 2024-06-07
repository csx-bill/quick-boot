package com.quick.online.entity;

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
@Schema(name = "数据源信息")
public class SysDataSource extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @Schema(description = "数据源名称")
    private String name;

    @Schema(description = "数据库类型")
    private String dbType;

    @Schema(description = "驱动类")
    private String dbDriver;

    @Schema(description = "数据源地址")
    private String dbUrl;

    @Schema(description = "用户名")
    private String dbUsername;

    @Schema(description = "密码")
    private String dbPassword;

}
