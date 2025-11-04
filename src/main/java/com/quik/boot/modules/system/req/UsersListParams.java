package com.quik.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsersListParams {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "项目名称")
    private String username;


}
