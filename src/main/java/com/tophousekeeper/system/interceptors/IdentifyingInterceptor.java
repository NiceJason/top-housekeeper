package com.tophousekeeper.system.interceptors;

import com.tophousekeeper.system.security.I_Identifying;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author NiceBin
 * @description: 验证码过滤器，帮忙验证有需要验证码的请求，不帮忙生成验证码
 * @date 2019/7/23 15:06
 */
@Component
public class IdentifyingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入拦截器");
        HttpSession session = request.getSession();
        I_Identifying identifying= (I_Identifying)session.getAttribute(I_Identifying.IDENTIFYING);
        if(identifying!=null){
            identifying.checkIdentifying(request);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
