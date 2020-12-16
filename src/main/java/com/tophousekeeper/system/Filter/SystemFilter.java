package com.tophousekeeper.system.Filter;

import com.tophousekeeper.system.SystemHttpServletRequestWrapper;
import com.tophousekeeper.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther: NiceBin
 * @description: 系统的拦截器，注册在FilterConfig类中进行
 *               不能使用@WebFilter，因为Filter要排序
 *               1.对ServletRequest进行封装
 *               2.防止CSRF，检查http头的Referer字段
 * @date: 2020/12/15 15:32
 */
@Component
public class SystemFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(SystemFilter.class);
    @Autowired
    private Environment environment;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("系统拦截器SystemFilter开始加载");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SystemHttpServletRequestWrapper requestWrapper = new SystemHttpServletRequestWrapper((HttpServletRequest) request);

        //检测http的Referer字段，不允许跨域访问
        String hostPath = environment.getProperty("server.host-path");
        String referer = requestWrapper.getHeader("Referer");
        if(!Tool.isNull(referer)){
            if(referer.lastIndexOf(hostPath)!=0){
                ((HttpServletResponse)response).setStatus(HttpStatus.FORBIDDEN.value());//设置错误状态码
                return;
            }
        }
        chain.doFilter(requestWrapper,response);
    }

    @Override
    public void destroy() {

    }
}
