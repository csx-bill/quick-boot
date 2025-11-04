package com.quik.boot.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "项目页面")
public class SysPages extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "页面名称")
    private String pageName;

    @Schema(description = "页面描述")
    private String pageDescription;

    @Schema(description = "页面JSON")
    @TableField(value = "`schema`")
    private String schema;

    @Schema(description = "项目ID")
    private Long projectId;

}
