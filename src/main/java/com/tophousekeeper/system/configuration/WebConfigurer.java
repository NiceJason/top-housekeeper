package com.tophousekeeper.system.configuration;

import com.tophousekeeper.system.interceptors.IdentifyingInterceptor;
import com.tophousekeeper.system.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/6/25 15:56
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;
    @Autowired
    private IdentifyingInterceptor identifyingInterceptor;

    //静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //举个例子
        //registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/static/dist/");
    }

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(identifyingInterceptor).addPathPatterns("/access/login","/access/registered");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/user");
    }
}
