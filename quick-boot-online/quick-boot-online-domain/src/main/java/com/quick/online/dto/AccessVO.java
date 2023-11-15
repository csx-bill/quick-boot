package com.quick.online.dto;

import com.quick.online.entity.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "AccessVO")
public class AccessVO extends Access {
    @Schema(description = "字段信息")
    private List<SysTableColumnVO> columns;

    @Schema(description = "查询方式")
    private List<OptionsVO> queryTypeOptions;

    @Schema(description = "显示控件")
    private List<OptionsVO> showTypeOptions;

    @Schema(description = "join类型")
    private List<OptionsVO> dictTableJoinOptions;
}
