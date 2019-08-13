package com.tophousekeeper.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/8/12 8:51
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController {

    @RequestMapping("/getNavegationURLs")
    public String getNavegationURLs(HttpServletRequest request){
        return null;
    }
}
