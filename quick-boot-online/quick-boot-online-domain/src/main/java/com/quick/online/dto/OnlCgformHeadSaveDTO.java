package com.quick.online.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "保存")
public class OnlCgformHeadSaveDTO {
    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "表类型")
    private int tableType;

    @Schema(description = "表版本")
    private int tableVersion = 1;

    @Schema(description = "表描述")
    private String tableTxt;

    @Schema(description = "是否多选")
    private String isCheckbox;

    @Schema(description = "是否数据库同步")
    private String isDbSynch = "N";

    @Schema(description = "是否分页")
    private String isPage;

    @Schema(description = "是否树形结构")
    private String isTree;

    @Schema(description = "ID类型")
    private String idType;

    @Schema(description = "查询模式")
    private String queryMode;

    @Schema(description = "关联类型")
    private Integer relationType;

    @Schema(description = "子表字符串")
    private String subTableStr;

    @Schema(description = "标签排序号")
    private Integer tabOrderNum;

    @Schema(description = "树形结构父ID字段")
    private String treeParentIdField;

    @Schema(description = "树形结构ID字段")
    private String treeIdField;

    @Schema(description = "树形结构字段名")
    private String treeFieldName;

    @Schema(description = "模板ID")
    private Long genTemplateId;
}
