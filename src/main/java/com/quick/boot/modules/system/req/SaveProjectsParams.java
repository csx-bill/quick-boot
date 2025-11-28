package com.quick.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveProjectsParams {

    @Schema(description = "logo")
    private String logo;

    @Schema(description = "项目名称")
    @NotBlank
    private String projectName;

    @Schema(description = "项目描述")
    private String projectDescription;
}
