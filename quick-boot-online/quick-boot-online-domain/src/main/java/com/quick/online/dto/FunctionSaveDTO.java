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
public class FunctionSaveDTO implements Serializable {
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

    @Schema(description = "返回值类型")
    private String returnType;

    /**
     * 参数列表，每个参数的类型都是 String。
     * 用 , 分割的字符串 比 [JSONArray] 更好，例如 array,item ，更直观，还方便拼接函数。
     */
    @Schema(description = "参数列表")
    private String arguments;

    @Schema(description = "详细描述")
    private String detail;

    /**
     * 允许的最低版本号，只限于GET,HEAD外的操作方法。
     * TODO 使用 requestIdList 替代 version,tag,methods
     */
    @Schema(description = "版本")
    private int version;

    @Schema(description = "标签")
    private String tag;

    /**
     * 允许的操作方法。
     * null - 允许全部
     * TODO 使用 requestIdList 替代 version,tag,methods
     */
    @Schema(description = "允许的操作方法")
    private String methods;


}
