package com.tophousekeeper.system.configuration;

import com.tophousekeeper.system.interceptors.IdentifyingInterceptor;
import com.tophousekeeper.system.interceptors.LoginInterceptor;
import com.tophousekeeper.system.interceptors.NormalInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NiceBin
 * @description: Spring内相关的配置
 *               1.静态资源路径映射
 *               2.拦截器添加
 * @date 2019/6/25 15:56
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;
    @Autowired
    NormalInterceptor normalInterceptor;
    @Autowired
    IdentifyingInterceptor identifyingInterceptor;

    //静态资源路径
    List<String> resources= new ArrayList<String>(){
        {
            add("/image/**");
            add("/css/**");
            add("/js/**");
        }
    };

    //静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //举个例子
        //registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/static/dist/");
    }

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("执行了addInterceptors");
        registry.addInterceptor(normalInterceptor).addPathPatterns("/**").excludePathPatterns(resources);
        registry.addInterceptor(loginInterceptor).addPathPatterns("/user");
        registry.addInterceptor(identifyingInterceptor).addPathPatterns("/access/login", "/access/registered");
    }
}
