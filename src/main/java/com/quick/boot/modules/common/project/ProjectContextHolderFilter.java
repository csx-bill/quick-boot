package com.quick.boot.modules.common.project;

import com.quick.boot.modules.common.constant.CommonConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 项目上下文过滤器
 * 用于从HTTP请求头中提取项目ID并设置到线程本地变量中
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 设置最高优先级，确保最先执行
public class ProjectContextHolderFilter extends GenericFilterBean {

    /** 未定义字符串常量 */
    private final static String UNDEFINED_STR = "undefined";

    /**
     * 过滤器核心方法
     * @param servletRequest 请求对象
     * @param servletResponse 响应对象
     * @param filterChain 过滤器链
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 从请求头中获取项目ID
        String headerProjectId = request.getHeader(CommonConstants.X_PROJECT_ID);
        log.debug("获取header中的项目ID为:{}", headerProjectId);

        // 验证项目ID是否有效（非空且不是"undefined"）
        if (StringUtils.hasText(headerProjectId) && !UNDEFINED_STR.equals(headerProjectId)) {
            // 将项目ID设置到线程本地变量中
            ProjectContextHolder.setProjectId(Long.parseLong(headerProjectId));
        }

        // 继续执行过滤器链
        filterChain.doFilter(request, response);

        // 清理线程本地变量，防止内存泄漏
        ProjectContextHolder.clear();
    }
}