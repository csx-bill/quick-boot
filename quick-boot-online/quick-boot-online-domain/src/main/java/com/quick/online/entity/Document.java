package com.quick.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "APIJSON接口文档")
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private String id;

    @Schema(description = "是否为 DEBUG: 0-否，1-是。")
    private Integer debug;

    @Schema(description = "用户ID")
    @TableField(value = "userId")
    private String userId;

    @Schema(description = "测试账号id")
    @TableField(value = "testAccountId")
    private String testAccountId;

    /**
     * 接口版本号
     * <=0 - 不限制版本，任意版本都可用这个接口；
     * >0 - 在这个版本添加的接口。
     *
     * 可在给新版文档前调高默认值，新增的测试用例就不用手动设置版本号了。
     */
    @Schema(description = "接口版本号")
    private Integer version;

    @Schema(description = "接口名称")
    private String name;

    /**
     * PARAM - GET  url parameters,
     * FORM - POST  application/www-x-form-url-encoded,
     * JSON - POST  application/json
     */
    @Schema(description = "PARAM - GET  url parameters,\n" +
            "FORM - POST  application/www-x-form-url-encoded,\n" +
            "JSON - POST  application/json")
    private String type;

    @Schema(description = "请求地址")
    private String url;


    @Schema(description = "请求\n" +
            "用json格式会导致强制排序，而请求中引用赋值只能引用上面的字段，必须有序。")
    private String request;

    @Schema(description = "从 request 映射为实际的 APIJSON 请求 JSON")
    private String apijson;

    @Schema(description = "为 SQLAuto 留的字段")
    private String sqlauto;

    @Schema(description = "")
    private String standard;

    @Schema(description = "请求头 Request Header：\n" +
            "key: value ")
    private String header;

    @Schema(description = "创建时间")
    private LocalDateTime date;


    @Schema(description = "描述")
    private String detail;

    @Schema(description = "接口权限")
    private String permission;


}
