package com.quik.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProjectsPageParams {

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "用户ID",hidden = true)
    private Long userId;
}
