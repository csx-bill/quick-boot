package com.quick.modules.online.vo;

import com.quick.modules.online.entity.Access;
import com.quick.modules.online.entity.SysTableColumn;
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
    private List<SysTableColumn> columns;
}
