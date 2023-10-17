package com.quick.modules.online.vo;

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
@Schema(name = "OptionsVO")
public class SelectOptionsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(description = "选中值")
    private String value;
    @Schema(description = "选项值")
    private List<OptionsVO> options;

}
