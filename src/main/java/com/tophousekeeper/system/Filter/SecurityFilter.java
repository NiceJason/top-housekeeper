package com.tophousekeeper.system.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tophousekeeper.system.security.SecurityServletRequest;
import com.tophousekeeper.system.security.XssStringJsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author NiceBin
 * @description: 安全拦截器
 *               过滤输入的敏感字符，防止XSS攻击
 * @date 2019/6/27 16:13
 */
@Component
@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*", asyncSupported = true)
public class SecurityFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        SecurityServletRequest securityServletRequest = new SecurityServletRequest(httpServletRequest);
        chain.doFilter(securityServletRequest,response);
    }

    @Override
    public void destroy() {

    }

    /**
     * 过滤json类型的XSS攻击
     */
    @Bean
    @Primary
    public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
        //解析器
        ObjectMapper objectMapper = builder.createXmlMapper(false).build(); //注册xss解析器
        SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
        xssModule.addSerializer(new XssStringJsonSerializer());
        objectMapper.registerModule(xssModule);
        // 返回
        return objectMapper;
    }
}

