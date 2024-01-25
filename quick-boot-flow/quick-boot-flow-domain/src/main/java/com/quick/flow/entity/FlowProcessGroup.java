package com.quick.flow.entity;

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
@Schema(name = "流程分组")
public class FlowProcessGroup extends BaseEntity implements Serializable {

    @Schema(name = "分组名")
    private String groupName;

    @Schema(name = "排序")
    private Integer sort;

}
