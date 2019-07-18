package com.tophousekeeper.controller;

import com.tophousekeeper.entity.Identifying;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/3 19:02
 */
@Controller
@RequestMapping("/access")
public class LoginController {

    //存储进Session变量Key
    //用户
    private static String S_USER = "user";
    //验证码生成时间
    private static String S_IDENTIFYING = "identifying";

    @Autowired
    LoginService loginService;

    /**
     * 注册
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/registered")
    public String registered(HttpServletRequest request) throws Exception {
        loginService.registered(request);
        return Tool.quickJson(SystemStaticValue.ACTION_RESULT, "注册成功");
    }

    /**
     * 登录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        loginService.checkIdentifying(request);

        User user = loginService.login(request);
        HttpSession session = request.getSession(true);
        session.setAttribute(S_USER, user);
        return Tool.quickJson(SystemStaticValue.ACTION_RESULT, "登录成功",
                SystemStaticValue.REDIRECT_URL, "/welcome");
    }

    /**
     * 注销
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, ModelAndView modelAndView) {

        RedisTemplate redisTemplate = new RedisTemplate();
        request.getSession().removeAttribute(S_USER);
        modelAndView.addObject(SystemStaticValue.ACTION_RESULT, "注销成功");
        modelAndView.setViewName("/welcome/welcome");
        return modelAndView;
    }

    /**
     * 获取验证码信息
     * @param request
     * @return
     * @throws NoSuchAlgorithmException
     */
    @ResponseBody
    @RequestMapping("/identifying")
    public String getIdentifying(HttpServletRequest request) throws NoSuchAlgorithmException {
           Identifying identifying = loginService.getIdentifying(request);
           HttpSession session = request.getSession();
           //存入验证类
           session.setAttribute(S_IDENTIFYING,identifying);
           return JSONObject.fromObject(identifying).toString();
    }
}
