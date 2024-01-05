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
@Schema(name = "保存-部门信息")
public class SysDeptSaveDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "父ID")
    private String parentId;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态")
    private String status;

}
