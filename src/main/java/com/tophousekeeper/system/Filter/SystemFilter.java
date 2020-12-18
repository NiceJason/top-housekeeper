package com.tophousekeeper.system.Filter;

import com.tophousekeeper.system.SystemHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: NiceBin
 * @description: 系统的拦截器，注册在FilterConfig类中进行
 *               不能使用@WebFilter，因为Filter要排序
 *               目前实现功能如下：
 *               1.对ServletRequest进行封装
 * @date: 2020/12/15 15:32
 */
@Component
public class SystemFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(SystemFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("系统拦截器SystemFilter开始加载");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SystemHttpServletRequestWrapper requestWrapper = new SystemHttpServletRequestWrapper((HttpServletRequest) request);

        //设置允许跨域访问
        HttpServletResponse respon = (HttpServletResponse)response;
        respon.addHeader("Access-Control-Allow-Origin", "*");
        respon.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        chain.doFilter(requestWrapper,response);
    }

    @Override
    public void destroy() {

    }
}
