package com.quick.online.dto;

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
@Schema(name = "查询")
public class GenTemplatePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "模板名称")
    private String templateName;
}
