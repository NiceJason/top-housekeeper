package com.tophousekeeper.controller;

import com.tophousekeeper.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WelcomeController {

    @RequestMapping(value={"/welcome","/"})
    public ModelAndView welcome(HttpServletRequest request,ModelAndView modelAndView){

        User user = (User) request.getSession().getAttribute("user");
        if(user!=null){
            modelAndView.addObject("userName",user.getEmail());
        }
        modelAndView.setViewName("/welcome/welcome");
        return modelAndView;
    }

}
