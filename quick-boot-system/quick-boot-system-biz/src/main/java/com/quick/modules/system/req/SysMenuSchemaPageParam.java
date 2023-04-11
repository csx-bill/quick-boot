package com.quick.modules.system.req;

import com.quick.common.util.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "页面配置分页查询参数")
public class SysMenuSchemaPageParam extends BasePageParam {

    @Schema(description = "菜单id")
    private String menuId;

    @Schema(description = "版本")
    private String version;
}
