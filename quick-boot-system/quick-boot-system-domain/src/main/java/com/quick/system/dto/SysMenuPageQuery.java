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
@Schema(name = "查询-菜单信息")
public class SysMenuPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "状态")
    private String status;

}
