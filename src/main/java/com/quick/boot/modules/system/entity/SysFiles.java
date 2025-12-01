package com.quick.boot.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
/**
 * 文件管理表
 */
@Data
@Schema(description = "文件")
public class SysFiles extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "存储桶名称")
    private String bucketName;

    @Schema(description = "文件夹名称")
    private String dir;

    @Schema(description = "原始文件名")
    private String original;

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "项目ID")
    private Long projectId;
}
