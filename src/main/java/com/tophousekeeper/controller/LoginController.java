package com.tophousekeeper.controller;

import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView registered(@RequestBody User user, Model model) throws Exception {
        loginService.registered(user);
        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("/welcome/welcome");
        modelAndView.addObject("testInfo", "kakak");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/login")
    public User login(User checkUser) {

        return loginService.login(checkUser);
    }
}
