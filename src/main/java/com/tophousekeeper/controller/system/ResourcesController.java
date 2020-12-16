package com.tophousekeeper.controller.system;

import com.tophousekeeper.entity.system.Response;
import com.tophousekeeper.service.system.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author NiceBin
 * @description: 资源获取的入口
 * @date 2019/8/12 8:51
 */
@Controller
@RequestMapping("/resources")
public class ResourcesController {

    @Autowired
    SystemService systemService;

    /**
     * 获取导航栏信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getNavegationURLs")
    public Response getNavegationURLs() throws Exception{

        return new Response().success(systemService.getNavegationURLs());
    }
}
