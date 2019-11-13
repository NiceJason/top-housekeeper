package com.tophousekeeper.system.interceptors;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import com.tophousekeeper.system.security.I_Identifying;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NiceBin
 * @description: 验证码过滤器，帮忙验证有需要验证码的请求，不帮忙生成验证码
 *               会对多次非法验证的ip进行封禁
 * @date 2019/7/23 15:06
 */
@Component
public class IdentifyingInterceptor implements HandlerInterceptor {

    //非法过滤器,key为ip地址,value为illegalInfo。
    Map<String,IllegalInfo> ipFilter = new HashMap();

    //错误信息，内部类
    class IllegalInfo{
        //非法次数
        int illegalCount = 0;
        //被封禁时间，当时时间-封禁时间>固定时间才解封
        Timestamp banTime = null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!isRequestFilter(request)){
            HttpSession session = request.getSession();
            I_Identifying identifying= (I_Identifying)session.getAttribute(I_Identifying.IDENTIFYING);
            if(identifying!=null){
                identifying.checkIdentifying(request);
            }else {
                //应该携带验证码信息的，结果没有携带，那就是个非法请求
                addIllegalCount(request);
                throw new SystemException(SystemStaticValue.Illegal_EXCEPTION_CODE,"非法访问");
            }
            return true;
        }else{
            throw new SystemException(SystemStaticValue.Illegal_EXCEPTION_CODE,"该ip非法登录次数过多，封禁一段时间");
        }


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * HttpSession里会有key为illegalCount的变量来记录是否非法访问次数
     * 如果超过，则看封禁的时间到了没，如果到了，则清空重置
     * 被过滤返回true
     * @param request
     * @return
     */
    private boolean isRequestFilter(HttpServletRequest request){

        String ip = Tool.getConnectIp(request);
        IllegalInfo illegalInfo = ipFilter.get(ip);

        if(illegalInfo!=null&&illegalInfo.illegalCount> SystemStaticValue.IDENTIFYING_ILLEGAL_STANDARD){
            Timestamp banTime = illegalInfo.banTime;
            Timestamp nowDate = new Timestamp((new Date()).getTime());
            Long diff = (nowDate.getTime()-banTime.getTime())/(1000*60);
            if(diff<=SystemStaticValue.IDENTIFYING_ILLEGAL_BAN){
                return true;
            }else{
                //重置信息
                ipFilter.remove(ip);
            }
        }
        return false;
    }

    /**
     * 增加该ip非法访问的次数，如果不存在则创建
     * @param request
     */
    private void addIllegalCount(HttpServletRequest request){
        String ip = Tool.getConnectIp(request);
        IllegalInfo illegalInfo = ipFilter.get(ip);
        if(illegalInfo == null){
            illegalInfo = new IllegalInfo();
            illegalInfo.banTime = new Timestamp(new Date().getTime());
            ipFilter.put(ip,illegalInfo);
        }
        illegalInfo.illegalCount++;

    }
}
