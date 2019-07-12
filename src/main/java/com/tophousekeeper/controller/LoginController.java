package com.tophousekeeper.controller;

import com.alibaba.fastjson.JSONObject;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
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

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/3 19:02
 */
@Controller
@RequestMapping("/access")
public class LoginController {

    @Autowired
    LoginService loginService;

    /**
     * 注册
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/registered")
    public String registered(@RequestBody User user) throws Exception {
        loginService.registered(user);
        return Tool.quickJson(SystemStaticValue.ACTION_RESULT, "注册成功");
    }

    /**
     * 登录
     *
     * @param checkUser
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    public String login(@RequestBody User checkUser, HttpServletRequest request) {
        User user = loginService.login(checkUser);
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        return Tool.quickJson(SystemStaticValue.ACTION_RESULT, "登录成功",
                SystemStaticValue.REDIRECT_URL, "/welcome");
    }

    /**
     * 注销
     *
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, ModelAndView modelAndView) {
        RedisTemplate redisTemplate = new RedisTemplate();
        request.getSession().removeAttribute("user");
        modelAndView.addObject(SystemStaticValue.ACTION_RESULT, "注销成功");
        modelAndView.setViewName("/welcome/welcome");
        System.out.println("根目录为：" + ClassUtils.getDefaultClassLoader().getResource("").getPath());
        return modelAndView;
    }
}
