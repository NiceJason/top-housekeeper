package com.tophousekeeper.system.configuration;

import com.tophousekeeper.system.SystemStaticValue;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @auther: NiceBin
 * @description: 错误页面配置，这里配置主要是为了向Web容器配置错误页面
 *               当错误没有到达Spring的Controller时
 *               是无法用@ControllerAdvice全局异常来进行统一处理的
 * @date: 2020/4/30 11:14
 */
@Component
public class ErrorPageConfig implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, SystemStaticValue.ERROR_PAGE_PATH+"/400/错误请求");
        ErrorPage error405Page = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, SystemStaticValue.ERROR_PAGE_PATH+"/405/不支持当前方法");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, SystemStaticValue.ERROR_PAGE_PATH+"/404/资源不存在");
        ErrorPage error500Page = new ErrorPage(Exception.class,SystemStaticValue.ERROR_PAGE_PATH+"/500/服务器异常");
        registry.addErrorPages(error400Page,error405Page,error404Page,error500Page);

    }
}
