package com.quick.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quick.common.aspect.annotation.Dict;
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
@Schema(name = "用户信息")
public class SysUser extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "头像")
    private String avatar;

    @Dict(dictCode = "sys_user_status")
    @Schema(description = "状态")
    private String status;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    @TableField(exist = false)
    private String deptName;

}
