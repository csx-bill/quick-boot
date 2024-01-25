package com.quick.online.entity;

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
@Schema(name = "表单Schema")
public class AccessSchema extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "表单ID")
    private Long accessId;

    @Schema(description = "schema")
    private String schema;

}
