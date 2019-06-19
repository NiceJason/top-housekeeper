package com.tophousekeeper.controller;

import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/registered")
    public String registered(@RequestBody User user, HttpServletRequest req) throws Exception {
//        throw new Exception("测试错误");
            req.getParameterMap();
           loginService.registered(user);
           return "/welcome/welcome";
    }

    @ResponseBody
    @RequestMapping("/login")
    public User login(User checkUser){
        return loginService.login(checkUser);
    }
}
