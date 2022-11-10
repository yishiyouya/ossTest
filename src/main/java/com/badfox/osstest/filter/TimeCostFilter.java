package com.badfox.osstest.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 统计各个方法响应耗时
 *
 * @author 15210
 */
@Slf4j
@WebFilter(filterName = "MyTimeCostFilter", urlPatterns = "/*")
public class TimeCostFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        filterChain.doFilter(servletRequest, servletResponse);

        stopWatch.stop();
        log.info("url: {}, cost: {}.\n",
                httpRequest.getRequestURI(),
                stopWatch.getLastTaskTimeMillis());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
