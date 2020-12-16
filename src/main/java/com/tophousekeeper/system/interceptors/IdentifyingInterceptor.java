package com.tophousekeeper.system.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.security.I_Identifying;
import com.tophousekeeper.util.HttpHelper;
import com.tophousekeeper.util.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NiceBin
 * @description: 验证码过滤器，帮忙验证有需要验证码的请求，不帮忙生成验证码
 * 会对多次非法验证的ip进行封禁
 * @date 2019/7/23 15:06
 */
@Component
public class IdentifyingInterceptor implements HandlerInterceptor {

    //目前只对以下请求类型进行检测
    private final String NORMAL_TYPE = "application/x-www-form-urlencoded";
    private final String JSON_TYPE = "application/json";
    private final String CHECK_TYPE = "application/x-www-form-urlencoded,application/json";

    //非法过滤器,key为ip地址,value为illegalInfo。
    Map<String, IllegalInfo> ipFilter = new HashMap();

    //错误信息，内部类
    class IllegalInfo {
        //非法次数
        int illegalCount = 0;
        //被封禁时间，当时时间-封禁时间>固定时间才解封
        Timestamp banTime = null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //解析后的参数
        HashMap<String, String> params = new HashMap<>();
        String contentType = request.getContentType();

        //直接前台URL访问，是没有类型的
        if (!Tool.isNull(contentType) && CHECK_TYPE.contains(contentType)) {
            String body = HttpHelper.getBodyString(request);
            //转码
            String param = URLDecoder.decode(body, "utf-8");

            if (!Tool.isNull(param)) {
                //接下来进行数据解析
                if (NORMAL_TYPE.equals(contentType)) {
                    String[] arr = param.split("=");
                    for (int i = 0; i + 1 < arr.length; i = i + 2) {
                        params.put(arr[i], arr[i + 1]);
                    }
                } else if (JSON_TYPE.equals(contentType)) {
                    JSONObject jsonObject = JSONObject.parseObject(param);
                    for (String key : jsonObject.keySet()
                    ) {
                        params.put(key, (String) jsonObject.get(key));
                    }
                }
            }

            if (!isLegalRequest(request)) {
                HttpSession session = request.getSession();
                I_Identifying identifying = (I_Identifying) session.getAttribute(I_Identifying.IDENTIFYING);
                if (identifying != null) {
                    identifying.checkIdentifying(params, session);
                } else {
                    //应该携带验证码信息的，结果没有携带，那就是个非法请求
                    addIllegalCount(request);
                    throw new SystemException(SystemStaticValue.ILLEGAL_EXCEPTION_CODE, "非法访问");
                }
            } else {
                throw new SystemException(SystemStaticValue.ILLEGAL_EXCEPTION_CODE, "该ip非法登录次数过多，封禁一段时间");
            }
        }
        return true;
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
     *
     * @param request
     * @return
     */
    private boolean isLegalRequest(HttpServletRequest request) {

        String ip = Tool.getConnectIp(request);
        IllegalInfo illegalInfo = ipFilter.get(ip);

        if (illegalInfo != null && illegalInfo.illegalCount > SystemStaticValue.IDENTIFYING_ILLEGAL_STANDARD) {
            Timestamp banTime = illegalInfo.banTime;
            Timestamp nowDate = new Timestamp((new Date()).getTime());
            Long diff = (nowDate.getTime() - banTime.getTime()) / (1000 * 60);
            if (diff <= SystemStaticValue.IDENTIFYING_ILLEGAL_BAN) {
                return true;
            } else {
                //重置信息
                ipFilter.remove(ip);
            }
        }
        return false;
    }

    /**
     * 增加该ip非法访问的次数，如果不存在则创建
     *
     * @param request
     */
    private void addIllegalCount(HttpServletRequest request) {
        String ip = Tool.getConnectIp(request);
        IllegalInfo illegalInfo = ipFilter.get(ip);
        if (illegalInfo == null) {
            illegalInfo = new IllegalInfo();
            illegalInfo.banTime = new Timestamp(new Date().getTime());
            ipFilter.put(ip, illegalInfo);
        }
        illegalInfo.illegalCount++;

    }
}
