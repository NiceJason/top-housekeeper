package com.tophousekeeper.system.interceptors;

import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author NiceBin
 * @description: 常规的对Controller进行处理
 *                1.在访问时，根据Session检测User对象有没有，若再检测Cookie
 *                  取得Cookie中的账号，密码，是否自动登录信息
 *                  根据信息进行操作
 *                2.在Controller返回时，检测Session有没有User，如果有，添加用户信息传给前端
 * @date 2019/9/3 14:10
 */
@Component
public class NormalInterceptor implements HandlerInterceptor {

    @Autowired
    LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        if(request.getSession().getAttribute(loginService.USER_OBJ) == null){
            //检测是否是自动登录
            loginService.checkAutoLogin(request,response);
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        //检测登录信息
        User user = (User)request.getSession().getAttribute(loginService.USER_OBJ);
           if(user!=null){
               if(modelAndView!=null){
                   modelAndView.addObject("userName",user.getEmail());
               }

           }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
