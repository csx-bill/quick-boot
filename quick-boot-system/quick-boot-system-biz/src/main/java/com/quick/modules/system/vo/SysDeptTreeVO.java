package com.quick.modules.system.vo;

import com.quick.modules.system.entity.SysDept;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "部门树")
public class SysDeptTreeVO extends SysDept {

    private static final long serialVersionUID = 1L;

    @Schema(description = "子部门")
    private List<SysDeptTreeVO> children;
}
