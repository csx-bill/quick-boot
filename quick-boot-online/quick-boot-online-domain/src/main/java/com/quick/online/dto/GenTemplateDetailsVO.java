package com.quick.online.dto;

import com.quick.online.entity.GenTemplate;
import com.quick.online.entity.GenTemplateVariableJs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GenTemplateDetailsVO extends GenTemplate {
    @Schema(description = "变量集合")
    private List<GenTemplateVariableJs> variableJsList;

}
