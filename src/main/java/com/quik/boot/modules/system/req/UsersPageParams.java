package com.quik.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsersPageParams {

    @Schema(description = "项目名称")
    private String username;

    @Schema(description = "项目ID")
    private Long projectId;
}
