package com.quick.boot.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色关联表
 */
@Data
public class SysUserRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "项目ID")
    private Long projectId;

}
