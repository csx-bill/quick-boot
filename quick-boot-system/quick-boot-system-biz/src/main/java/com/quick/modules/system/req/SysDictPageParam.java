package com.quick.modules.system.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@Schema(name = "字典信息分页查询参数")
public class SysDictPageParam extends BasePageParam {
    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典编码")
    private String dictCode;
}
