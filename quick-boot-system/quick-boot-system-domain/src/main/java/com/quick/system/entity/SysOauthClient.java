package com.quick.system.entity;

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
@Schema(name = "客户端信息")
public class SysOauthClient extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "应用ID")
    public String clientId;

    @Schema(description = "应用秘钥")
    public String clientSecret;

    @Schema(description = "应用签约的所有权限, 多个用逗号隔开")
    public String contractScope;

    @Schema(description = "应用允许授权的所有URL, 多个用逗号隔开 （可以使用 * 号通配符）")
    public String allowUrl;

    @Schema(description = "是否打开 授权码 模式")
    public Boolean isCode;

    @Schema(description = "是否打开 隐藏 模式")
    public Boolean isImplicit;

    @Schema(description = "是否打开 密码 模式")
    public Boolean isPassword;

    @Schema(description = "是否打开 凭证 模式")
    public Boolean isClient;

    @Schema(description = "是否自动判断此 Client 开放的授权模式")
    public Boolean isAutoMode;

    @Schema(description = "是否在每次 Refresh-Token 刷新 Access-Token 时，产生一个新的 Refresh-Token")
    public Boolean isNewRefresh;

    @Schema(description = "Access-Token 保存的时间（单位：秒）")
    public long accessTokenTimeout;

    @Schema(description = "Refresh-Token 保存的时间（单位：秒）")
    public long refreshTokenTimeout;

    @Schema(description = "Client-Token 保存的时间（单位：秒）")
    public long clientTokenTimeout;

    @Schema(description = "Past-Client-Token 保存的时间（单位：秒）")
    public long pastClientTokenTimeout;
}
