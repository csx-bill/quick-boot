package com.quick.online.dto;

import com.quick.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysTableColumnVO")
public class SysTableColumnVO extends BaseEntity implements Serializable {
    @Schema(description = "表权限ID")
    private String accessId;

    @Schema(description = "字段名字")
    private String dbFieldName;

    @Schema(description = "字段备注")
    private String dbFieldTxt;

    @Schema(description = "是否主键")
    private String dbIsKey;

    @Schema(description = "表单显示")
    private String isShowForm;

    @Schema(description = "列表显示")
    private String isShowList;

    @Schema(description = "是否必填")
    private String isRequired;

    @Schema(description = "是否只读")
    private String isReadOnly;

    @Schema(description = "查询字段")
    private String isQuery;

    @Schema(description = "查询方式")
    private SelectOptionsVO queryType;

    @Schema(description = "显示控件")
    private SelectOptionsVO showType;

    @Schema(description = "字典table")
    private String dictTable;

    @Schema(description = "join类型")
    private SelectOptionsVO dictTableJoin;

    @Schema(description = "字典code")
    private String dictCode;

    @Schema(description = "字典Text")
    private String dictText;

    @Schema(description = "排序")
    private Integer sort;
}
