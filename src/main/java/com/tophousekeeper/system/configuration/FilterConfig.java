package com.tophousekeeper.system.configuration;

import com.tophousekeeper.system.Filter.SystemFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auther: NiceBin
 * @description: 为了排序Filter，如果Filter有顺序要求
 *               那么需要在此注册，设置order（值越低优先级越高）
 *               其他没顺序需要的，可以@WebFilter注册
 *               如@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*", asyncSupported = true)
 * @date: 2020/12/15 15:48
 */
@Configuration
public class FilterConfig {

    @Autowired
    SystemFilter systemFilter;
    /**
     * 注册SystemFilter，顺序为1，任何其他filter不能比他优先
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegist(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(systemFilter);
        filterRegistrationBean.setName("SystemFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
