package com.quick.boot.modules.system.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FilesPageParams {

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "原始文件名")
    private String original;
}
