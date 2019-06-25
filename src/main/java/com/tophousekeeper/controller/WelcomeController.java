package com.tophousekeeper.controller;

import com.tophousekeeper.entity.User;
import com.tophousekeeper.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WelcomeController {

    @Autowired
    private WelcomeService welcomeService;

    @RequestMapping(value={"/","/index","welcome"})
    public ModelAndView welcome(HttpServletRequest request,ModelAndView modelAndView){
        System.out.println("进入欢迎页面");
        System.out.println(welcomeService.selectByResourcesId(1));
        User user = (User) request.getSession().getAttribute("user");
        if(user!=null){
            modelAndView.addObject("userName",user.getEmail());
        }

        modelAndView.setViewName("/welcome/welcome");
        return modelAndView;
    }

}
