package com.tophousekeeper.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/8/15 10:30
 */
@Controller
@RequestMapping("/Navegation")
public class NavegationController {

    @RequestMapping("/jumpURL")
    public ModelAndView jumpURL(HttpServletRequest request,ModelAndView modelAndView){
           String src = request.getParameter("src");
           modelAndView.addObject("src",src);
           modelAndView.setViewName("/content/blog");
           return modelAndView;
    }
}
