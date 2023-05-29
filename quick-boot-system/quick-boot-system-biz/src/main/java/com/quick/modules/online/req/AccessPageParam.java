package com.quick.modules.online.req;

import com.quick.common.util.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "数据表权限配置分页查询参数")
public class AccessPageParam extends PageParam {
    @Schema(description = "表名")
    private String name;

    @Schema(description = "表别名")
    private String alias;
}
