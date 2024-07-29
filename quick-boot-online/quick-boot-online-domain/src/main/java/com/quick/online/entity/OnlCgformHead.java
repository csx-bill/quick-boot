package com.quick.online.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.common.constant.CommonConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "OnlCgformHead")
public class OnlCgformHead implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;


    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "表类型")
    private int tableType;

    @Schema(description = "表版本")
    private int tableVersion;

    @Schema(description = "表描述")
    private String tableTxt;

    @Schema(description = "是否多选")
    private String isCheckbox;

    @Schema(description = "是否数据库同步")
    private String isDbSynch;

    @Schema(description = "是否分页")
    private String isPage;

    @Schema(description = "是否树形结构")
    private String isTree;

    @Schema(description = "ID类型")
    private String idType;

    @Schema(description = "关联类型")
    private Integer relationType;

    @Schema(description = "子表字符串")
    private String subTableStr;

    @Schema(description = "附表排序序号")
    private Integer tabOrderNum;

    @Schema(description = "树形结构父ID字段")
    private String treeParentIdField;

    @Schema(description = "树形结构ID字段")
    private String treeIdField;

    @Schema(description = "树形结构字段名")
    private String treeFieldName;

    @Schema(description = "模板ID")
    private Long genTemplateId;

    @Schema(description = "数据库名")
    private String schema;


}
