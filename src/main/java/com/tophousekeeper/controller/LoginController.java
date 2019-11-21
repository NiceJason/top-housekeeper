package com.tophousekeeper.controller;

import com.alibaba.fastjson.JSONObject;
import com.tophousekeeper.entity.ImgIdentifying;
import com.tophousekeeper.service.LoginService;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.util.Tool;
import com.tophousekeeper.system.security.I_Identifying;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/3 19:02
 */
@Controller
@RequestMapping("/access")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 注册
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/registered")
    public String registered(HttpServletRequest request)  {
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
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception{

        String email = request.getParameter("email");
        loginService.checkEmail(email);
        String password = request.getParameter("password");
        loginService.checkPassword(password);

        loginService.login(request, response,email,password,false);

        return Tool.quickJson(SystemStaticValue.ACTION_RESULT, "登录成功",
                SystemStaticValue.REDIRECT_URL, "/welcome");
    }

    /**
     * 注销
     * 绝对能注销成功的，
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,HttpServletResponse response, ModelAndView modelAndView){

        loginService.logout(request,response);
        modelAndView.addObject(SystemStaticValue.ACTION_RESULT, "注销成功");
        modelAndView.setViewName("/welcome/welcome");
        return modelAndView;
    }

    /**
     * 获取验证码信息
     *
     * @param request
     * @return
     * @throws NoSuchAlgorithmException
     */
    @ResponseBody
    @RequestMapping("/identifying")
    public String getIdentifying(HttpServletRequest request) throws NoSuchAlgorithmException {
        ImgIdentifying identifying = new ImgIdentifying().getInstance(request);
        HttpSession session = request.getSession();
        //存入验证类
        session.setAttribute(I_Identifying.IDENTIFYING, identifying);
        return JSONObject.toJSONString(identifying);
    }
}
