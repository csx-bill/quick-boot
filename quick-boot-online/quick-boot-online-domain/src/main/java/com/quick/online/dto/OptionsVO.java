package com.quick.online.dto;

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
@Schema(name = "OptionsVO")
public class OptionsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "label")
    private String label;

    @Schema(description = "value")
    private String value;

}
