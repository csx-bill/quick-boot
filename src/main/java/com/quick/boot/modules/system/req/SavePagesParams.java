package com.quick.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SavePagesParams {

    @Schema(description = "页面名称")
    @NotBlank
    private String pageName;

    @Schema(description = "页面描述")
    private String pageDescription;

    @Schema(description = "页面JSON")
    private String schema;


}
