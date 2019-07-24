package com.tophousekeeper.system.interceptors;

import com.tophousekeeper.entity.User;
import com.tophousekeeper.system.SystemException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/6/25 16:30
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入登录过滤器");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user !=null){
            return true;
        }else {
            //这里应该给未登录信息，并打开登录的模态窗口
            System.out.println("被过滤了");
            throw new SystemException("100","请先登录");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
