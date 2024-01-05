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
@Schema(name = "保存-远程函数")
public class FunctionPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "是否为 DEBUG: 0-否，1-是。")
    private int debug;

    /**
     * 语言：Java(java), JavaScript(js), Lua(lua), Python(py), Ruby(ruby), PHP(php) 等，
     * NULL 默认为 Java，JDK 1.6-11 默认支持 JavaScript，JDK 12+ 需要额外依赖 Nashron/Rhiro 等
     * js 引擎库，其它的语言需要依赖对应的引擎库，并在 ScriptEngineManager 中注册
     */
    @Schema(description = "语言")
    private String language;

    @Schema(description = "方法名")
    private String name;

}
