package com.tophousekeeper.controller.system;

import com.tophousekeeper.service.system.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/8/12 8:51
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController {

    @Autowired
    SystemService systemService;

    @ResponseBody
    @RequestMapping("/getNavegationURLs")
    public String getNavegationURLs(HttpServletRequest request){
        return systemService.getNavegationURLs();
    }
}
