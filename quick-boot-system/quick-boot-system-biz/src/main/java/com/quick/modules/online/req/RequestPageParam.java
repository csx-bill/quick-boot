package com.quick.modules.online.req;

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
@Schema(name = "请求参数校验配置分页查询参数")
public class RequestPageParam extends BasePageParam {

    @Schema(description = "标签")
    private String tag;
}
