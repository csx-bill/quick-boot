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
@Schema(name = "查询-APIJSON接口文档")
public class DocumentPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "是否为 DEBUG: 0-否，1-是。")
    private Integer debug;

    @Schema(description = "接口名称")
    private String name;

}
