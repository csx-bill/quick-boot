package com.quick.system.dto;

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
@Schema(name = "查询-数据权限")
public class SysDataRulePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "菜单id")
    private String menuId;

    @Schema(description = "规则名称")
    private String ruleName;

    @Schema(description = "状态")
    private String status;
}
