package com.tophousekeeper.controller;

import com.tophousekeeper.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NiceBin
 * @description: 关于用户操作的Controller
 * @date 2019/6/27 13:07
 */
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/homePage")
    public ModelAndView getHomePage(HttpServletRequest request,ModelAndView modelAndView){
        User user = (User) request.getSession().getAttribute("user");
        modelAndView.addObject("userName",user.getEmail());
        modelAndView.setViewName("/user/home-page");
        return modelAndView;
    }
}
