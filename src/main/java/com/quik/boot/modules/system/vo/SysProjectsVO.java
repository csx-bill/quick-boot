package com.quik.boot.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "项目VO")
public class SysProjectsVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "LOGO")
    private String logo;

    @Schema(description = "项目描述")
    private String projectDescription;

    @Schema(description = "用户类型: 管理员 admin,普通用户 user")
    private String userType;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
