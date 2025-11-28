package com.quick.boot.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * 用户表
 */
@Data
@Schema(description = "用户")
public class SysUsers extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@Schema(description = "主键id")
	private Long id;

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@Schema(description = "密码")
	private String password;

	/**
	 * 随机盐
	 */
	@JsonIgnore
	@Schema(description = "随机盐")
	private String salt;


	/**
	 * 锁定标记
	 */
	@Schema(description = "锁定标记")
	private String lockFlag;

	/**
	 * 手机号
	 */
	@Schema(description = "手机号")
	private String phone;

	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;


	/**
	 * 邮箱
	 */
	//@Schema(description = "邮箱")
	private String email;

}
