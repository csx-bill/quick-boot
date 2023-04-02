package com.quick.modules.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "表权限配置")
public class Access implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "是否为 DEBUG: 0-否，1-是。")
    private int debug;

    @TableField(value = "`schema`")
    @Schema(description = "数据库名/模式")
    private String schema;

    @TableField(value = "`name`")
    @Schema(description = "表名")
    private String name;

    @Schema(description = "表别名")
    private String alias;

    @Schema(description = "创建时间")
    private LocalDateTime date;
}
