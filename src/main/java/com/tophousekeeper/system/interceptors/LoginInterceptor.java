package com.tophousekeeper.system.interceptors;

import com.tophousekeeper.system.CommonStaticValue;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.util.HttpHelper;
import com.tophousekeeper.util.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author NiceBin
 * @description:    根据Token,验证是否是登录状态
 * @date 2019/6/25 16:30
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入登录拦截器");

        String token = request.getHeader(CommonStaticValue.TOKEN);

        if(Tool.isTokenAlive(request)){
            return true;
        }else {
            //这里应该给未登录信息，并打开登录的模态窗口
            System.out.println("被过滤了");
//            throw new SystemException(SystemStaticValue.PERMISSIONS_EXCEPTION,"请先登录");
            return HttpHelper.errorPageDirect(response,SystemStaticValue.PERMISSIONS_EXCEPTION,"请先登录或点链接进入");
        }
    }

    /**
     * Controller处理完，视图渲染之前
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
