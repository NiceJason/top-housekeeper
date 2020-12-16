package com.tophousekeeper.controller;

import com.alibaba.fastjson.JSONObject;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.entity.access.Login;
import com.tophousekeeper.entity.access.RegisterInfo;
import com.tophousekeeper.entity.identifying.ImgIdentifyInfo;
import com.tophousekeeper.entity.identifying.ImgIdentifying;
import com.tophousekeeper.entity.system.Response;
import com.tophousekeeper.service.LoginService;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.security.I_Identifying;
import com.tophousekeeper.util.Tool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/3 19:02
 */
@Controller
@RequestMapping("/access")
@Api("LoginController|登录控制类")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 注册
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/registered")
    @ApiOperation(value = "注册",notes = "注册账号",httpMethod = "POST",produces = MediaType.APPLICATION_JSON_VALUE)
    public Response registered(@RequestBody @Valid RegisterInfo registerInfo)  {
        loginService.registered(registerInfo);
        return new Response().success(Tool.quickJson(SystemStaticValue.ACTION_RESULT, "注册成功"));
    }

    /**
     * 登录
     * @param login
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login" )
    public Response login(@RequestBody @Valid Login login,HttpServletRequest request,HttpServletResponse response) throws Exception{

        User user = loginService.login(login,false,request,response);

        JSONObject data = new JSONObject();
        data.put("userName",user.getEmail());
        data.put(SystemStaticValue.REDIRECT_URL,"/welcome");

        return new Response().success(data);
    }

    /**
     * 注销
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request,HttpServletResponse response, ModelAndView modelAndView){

        loginService.logout(request,response);
        modelAndView.addObject(SystemStaticValue.ACTION_RESULT, "注销成功");
        modelAndView.setViewName("/welcome/welcome");
        return modelAndView;
    }

    /**
     * 获取验证码信息
     *
     * @param request
     * @return
     * @throws NoSuchAlgorithmException
     */
    @ResponseBody
    @RequestMapping(value = "/identifying")
    public Response getIdentifying(HttpServletRequest request,@RequestBody ImgIdentifyInfo imgIdentifyInfo) throws NoSuchAlgorithmException {
        ImgIdentifying identifying = new ImgIdentifying().getInstance(imgIdentifyInfo);
        HttpSession session = request.getSession();
        //存入验证类
        session.setAttribute(I_Identifying.IDENTIFYING, identifying);
        return new Response().success(identifying);
    }

    /**
     * 检测是否还在登录状态
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAliveUser",method = RequestMethod.GET)
    public Response getAliveUser(HttpServletRequest request){
        User user = loginService.getAliveUser(request);
        if(user!=null){
            return new Response().success(user.getEmail());
        }
        else{
            return new Response().success();
        }
    }
}
