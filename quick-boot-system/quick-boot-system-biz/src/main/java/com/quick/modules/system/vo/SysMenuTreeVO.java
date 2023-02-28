package com.quick.modules.system.vo;

import com.quick.modules.system.entity.SysMenu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "菜单树")
public class SysMenuTreeVO extends SysMenu {

    private static final long serialVersionUID = 1L;

    @Schema(description = "meta")
    private SysMenuTreeMetaVO meta;

    @Schema(description = "子菜单")
    private List<SysMenuTreeVO> children;
}
