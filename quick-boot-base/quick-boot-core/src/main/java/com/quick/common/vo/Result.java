package com.quick.common.vo;

import com.quick.common.constant.CommonConstant;
import com.quick.common.util.SpringBeanUtils;
import io.micrometer.tracing.Tracer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 接口返回对象
 * @param <T>
 */
@Getter
@Setter
@SuppressWarnings("ALL")
@Accessors(chain = true)
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 返回处理消息
	 */
	private String msg = CommonConstant.SUCCESS_MSG;

	/**
	 * 返回代码
	 */
	private Integer status = CommonConstant.SUCCESS_CODE;

	/**
	 * 返回数据对象 data
	 */
	private T data;

	private String traceId = SpringBeanUtils.getBean(Tracer.class).currentSpan().context().traceId();

	public Result() {

	}

	public Result(Integer status, T data, String msg) {
		this.status = status;
		this.data = data;
		this.msg = msg;
	}

	/**
	 * 请求成功消息
	 *
	 * @param data 结果
	 * @return RPC调用结果
	 */
	public static <E> Result<E> success(E data) {
		return new Result<>(CommonConstant.SUCCESS_CODE, data, CommonConstant.SUCCESS_MSG);
	}

	public static Result<Boolean> success() {
		return new Result<>(CommonConstant.SUCCESS_CODE, true, CommonConstant.SUCCESS_MSG);
	}

	/**
	 * 请求失败消息
	 *
	 * @param msg
	 * @return
	 */
	public static <E> Result<E> fail(int status, String msg) {
		return new Result<>(status, null, msg);
	}

	public static <E> Result<E> fail(String msg) {
		return new Result<>(CommonConstant.SC_INTERNAL_SERVER_ERROR_500, null, msg);
	}
}