package com.tophousekeeper.controller;

import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NiceBin
 * @description: 关于用户操作的Controller
 * @date 2019/6/27 13:07
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/home")
    public ModelAndView getHomePage(HttpServletRequest request,ModelAndView modelAndView){
        User user = (User) request.getSession().getAttribute(LoginService.USER_OBJ);
        modelAndView.addObject("userName",user.getEmail());
        modelAndView.setViewName("/user/home");
        return modelAndView;
    }
}
