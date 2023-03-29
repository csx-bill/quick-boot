package com.quick.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Schema(name = "页面配置信息")
public class SysMenuSchema implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "页面json")
    private String schemaJson;

    @TableId(type = IdType.INPUT)
    @Schema(description = "菜单id")
    private String menuId;
}
