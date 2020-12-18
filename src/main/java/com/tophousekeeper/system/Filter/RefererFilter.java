package com.tophousekeeper.system.Filter;

import com.tophousekeeper.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: NiceBin
 * @description: 对需要检测http头部Referer字段的路径进行检测
 * 因为并不是所有访问都需要防止CSRF攻击的
 * @date: 2020/12/18 16:17
 */
@WebFilter(filterName = "RefererFilter", urlPatterns = "/user/*", asyncSupported = true)
public class RefererFilter implements Filter {

    @Autowired
    private Environment environment;

    private final Logger log = LoggerFactory.getLogger(RefererFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //检测http的Referer字段，不允许跨域访问
        String hostPath = environment.getProperty("server.host-path");
        String referer = ((HttpServletRequest)request).getHeader("Referer");
        log.info("此次请求的Referer："+referer);

        if (!Tool.isNull(referer)) {
            if (referer.lastIndexOf(hostPath) != 0) {
                ((HttpServletResponse) response).setStatus(HttpStatus.FORBIDDEN.value());//设置错误状态码
                return;
            }
        }

        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
