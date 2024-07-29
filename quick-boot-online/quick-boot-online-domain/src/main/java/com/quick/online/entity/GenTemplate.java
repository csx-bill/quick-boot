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
@Schema(name = "模板")
public class GenTemplate extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "模板名称")
    private String templateName;
    @Schema(description = "模板代码")
    private String templateCode;
    @Schema(description = "模板描述")
    private String templateDesc;

}
