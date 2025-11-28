package com.quick.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdatePagesParams {

    @Schema(description = "页面名称")
    private String pageName;

    @Schema(description = "页面描述")
    private String pageDescription;

    @Schema(description = "页面JSON")
    private String schema;

    @Schema(description = "版本号")
    private Integer version;

}
