package com.quick.boot.modules.common.project;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * 项目上下文持有工具类
 * 使用TransmittableThreadLocal存储项目ID，支持线程池环境下的上下文传递
 */
@UtilityClass // Lombok注解，自动生成私有构造方法，确保工具类不可实例化
public class ProjectContextHolder {

    /**
     * 使用TransmittableThreadLocal存储项目ID
     * 相比普通ThreadLocal，支持线程池环境下的值传递
     */
    private final ThreadLocal<Long> THREAD_LOCAL_PROJECT = new TransmittableThreadLocal<>();

    /**
     * 设置项目ID到线程上下文
     * @param projectId 项目ID
     */
    public void setProjectId(Long projectId) {
        THREAD_LOCAL_PROJECT.set(projectId);
    }

    /**
     * 从线程上下文中获取项目ID
     * @return 项目ID，如果未设置则返回null
     */
    public Long getProjectId() {
        return THREAD_LOCAL_PROJECT.get();
    }

    /**
     * 清理线程上下文中的项目ID
     * 防止内存泄漏，建议在请求处理完成后调用
     */
    public void clear() {
        THREAD_LOCAL_PROJECT.remove();
    }
}