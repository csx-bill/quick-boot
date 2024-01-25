package com.quick.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quick.common.constant.CommonConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@MappedSuperclass
public class BaseEntity implements Serializable {
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

    @JsonIgnore
    @Schema(description = "删除状态")
    private Integer delFlag;
}
