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
@Schema(name = "流程发起人")
public class FlowProcessStarter extends BaseEntity implements Serializable {

    @Schema(name = "用户id或者部门id")
    private Long typeId;

    @Schema(name = "类型 user dept")
    private String type;

    @Schema(name = "流程id")
    private Long processId;

}
