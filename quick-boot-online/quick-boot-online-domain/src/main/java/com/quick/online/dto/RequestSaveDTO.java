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
@Schema(name = "保存-APIJSON接口请求参数校验")
public class RequestSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @Schema(description = "是否为 DEBUG: 0-否，1-是。")
    private Integer debug;

    @Schema(description = "方法")
    private String method;

    /**
     * GET,HEAD可用任意结构访问任意开放内容，不需要这个字段。
     * 其它的操作因为写入了结构和内容，所以都需要，按照不同的version选择对应的structure。
     *
     * 自动化版本管理：
     * Request JSON最外层可以传  “version”:Integer 。
     * 1.未传或 <= 0，用最新版。 “@order”:”version-“
     * 2.已传且 > 0，用version以上的可用版本的最低版本。 “@order”:”version+”, “version{}”:”>={version}”
     */
    @Schema(description = "版本")
    private Integer version;

    @Schema(description = "标签")
    private String tag;

    @Schema(description = "结构")
    private String structure;

    @Schema(description = "描述")
    private String detail;

}
