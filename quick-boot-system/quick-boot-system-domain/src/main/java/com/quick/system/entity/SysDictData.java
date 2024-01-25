package com.quick.system.entity;

import com.quick.common.aspect.annotation.Dict;
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
@Schema(name = "字典数据信息")
public class SysDictData extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "字典ID")
    private Long dictId;

    @Schema(description = "字典文本")
    private String dictText;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Dict(dictCode = "sys_dict_data_status")
    @Schema(description = "状态")
    private String status;


}
