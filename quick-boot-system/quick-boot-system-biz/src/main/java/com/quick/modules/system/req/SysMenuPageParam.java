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
@Schema(name = "菜单信息分页查询参数")
public class SysMenuPageParam extends BasePageParam {

    @Schema(description = "菜单名称")
    private String name;
}
