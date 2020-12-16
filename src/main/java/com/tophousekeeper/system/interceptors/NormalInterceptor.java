package com.tophousekeeper.system.interceptors;

import com.tophousekeeper.service.LoginService;
import com.tophousekeeper.system.CommonStaticValue;
import com.tophousekeeper.util.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author NiceBin
 * @description: 常规的对Controller进行处理
 *                1.在访问时，根据头部检测Token状态，若无或者过期了再检测Cookie
 *                  取得Cookie中的账号，密码，是否自动登录信息
 *                  根据信息进行操作
 * @date 2019/9/3 14:10
 */
@Component
public class NormalInterceptor implements HandlerInterceptor {

    @Autowired
    LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("此次请求来源于："+request.getRequestURL());
        String token = request.getHeader(CommonStaticValue.TOKEN);

        if(!JwtTool.isAlive(token)){
            //检测是否是自动登录
            loginService.checkAutoLogin(request,response);
        }
        return true;
    }

    /**
     * 在这里修改Controller的返回值是不行的
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
