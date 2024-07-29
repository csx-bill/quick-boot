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
public class OnlCgformField implements Serializable {
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

    @Schema(description = "表ID")
    private Long cgformHeadId;

    @Schema(description = "字段名字")
    private String dbFieldName;

    @Schema(description = "字段备注")
    private String dbFieldTxt;

    @Schema(description = "是否主键 0否 1是")
    private Integer dbIsKey;

    @Schema(description = "是否允许为空 0否 1是")
    private Integer dbIsNull;

    @Schema(description = "数据库字段类型")
    private String dbType;

    @Schema(description = "数据库字段长度")
    private Integer dbLength;

    @Schema(description = "小数点")
    private Integer dbPointLength;

    @Schema(description = "表字段默认值")
    private String dbDefaultVal;

    @Schema(description = "字典code")
    private String dictField;

    @Schema(description = "字典表")
    private String dictTable;

    @Schema(description = "字典Text")
    private String dictText;

    @Schema(description = "表单控件类型")
    private String fieldShowType;

    @Schema(description = "跳转URL")
    private String fieldHref;

    @Schema(description = "表单控件长度")
    private Integer fieldLength;

    @Schema(description = "表单字段校验规则")
    private String fieldValidType;

    @Schema(description = "字段是否必填")
    private String fieldMustInput;

    @Schema(description = "填值规则")
    private String fieldDefaultValue;

    @Schema(description = "是否查询条件 0否 1是")
    private Integer isQuery;

    @Schema(description = "表单是否显示 0否 1是")
    private Integer isShowForm;

    @Schema(description = "列表是否显示 0否 1是")
    private Integer isShowList;

    @Schema(description = "是否是只读（1是 0否）")
    private Integer isReadOnly;

    @Schema(description = "查询模式")
    private String queryMode;

    @Schema(description = "外键主表名")
    private String mainTable;

    @Schema(description = "外键主键字段")
    private String mainField;

    @Schema(description = "排序")
    private Integer orderNum;

    @Schema(description = "查询默认值")
    private String queryDefVal;

    @Schema(description = "查询配置字典text")
    private String queryDictText;

    @Schema(description = "查询配置字典code")
    private String queryDictField;

    @Schema(description = "查询配置字典table")
    private String queryDictTable;

    @Schema(description = "查询显示控件")
    private String queryShowType;

    @Schema(description = "查询字段校验类型")
    private String queryValidType;

    @Schema(description = "查询字段是否必填 1是 0否")
    private String queryMustInput;

    @Schema(description = "是否支持排序 1是 0否")
    private String sortFlag;

}
