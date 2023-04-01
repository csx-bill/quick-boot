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
@Schema(name = "字典数据信息分页查询参数")
public class SysDictDataPageParam extends BasePageParam {

    @Schema(description = "字典ID")
    private String dictId;

    @Schema(description = "字典文本")
    private String dictText;
}
