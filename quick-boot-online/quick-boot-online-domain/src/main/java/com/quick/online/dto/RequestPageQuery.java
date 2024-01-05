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
@Schema(name = "查询-APIJSON接口请求参数校验")
public class RequestPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "是否为 DEBUG: 0-否，1-是。")
    private Integer debug;

    @Schema(description = "方法")
    private String method;

    @Schema(description = "标签")
    private String tag;

}
