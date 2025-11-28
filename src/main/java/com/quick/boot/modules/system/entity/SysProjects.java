package com.quick.boot.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@Schema(description = "项目")
public class SysProjects extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "logo")
    private String logo;

    @Schema(description = "项目名称")
    private String projectName;


    @Schema(description = "项目描述")
    private String projectDescription;
}
