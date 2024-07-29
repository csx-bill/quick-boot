package com.quick.online.entity;

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
@Schema(name = "模板变量JS")
public class GenTemplateVariableJs extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "模板id")
    private Long templateId;
    @Schema(description = "js代码")
    private String variableJs;
    @Schema(description = "变量名")
    private String variableName;

}
