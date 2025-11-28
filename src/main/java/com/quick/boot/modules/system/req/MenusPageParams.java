package com.quick.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenusPageParams {

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "项目ID")
    @NotNull
    private Long projectId;
}
