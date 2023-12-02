package com.quick.system.api.dto;

import com.quick.common.entity.BaseEntity;
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
public class SysDictDataApiDTO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "字典ID")
    private String dictId;

    @Schema(description = "字典文本")
    private String dictText;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态")
    private String status;


}
