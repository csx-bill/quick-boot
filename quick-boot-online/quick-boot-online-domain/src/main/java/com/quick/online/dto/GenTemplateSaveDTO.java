package com.quick.online.dto;

import com.quick.online.entity.GenTemplateVariableJs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "保存")
public class GenTemplateSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "模板名称")
    private String templateName;
    @Schema(description = "模板代码")
    private String templateCode;
    @Schema(description = "模板描述")
    private String templateDesc;

    @Schema(description = "模板变量集合")
    private List<GenTemplateVariableJs> variableJsList;
}
