package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.system.running.SystemContext;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.util.Tool;
import com.tophousekeeper.system.security.EncrypRSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/18 8:15
 */
@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    //用来设置cookie，存放加密后的账户，主要是自动登录的时候用
    public final String USER_EMAIL = "userEmail";
    //用来设置cookie，存放加密后的密码，主要是自动登录的时候用
    public final String USER_PASSWORD = "userPassword";
    //用来设置cookie，自动登录标记，主要是自动登录的时候用
    public final String AUTOLOGIN = "autoLogin";
    //session存放user对象
    public final String USER_OBJ = "userObj";

    public void registered(HttpServletRequest request) {
        User user = null;

        String email = request.getParameter("email");
        checkEmail(email);
        //检测账号是否已经存在
        user = loginDao.selectByPrimaryKey(email);
        if(user != null)throw new SystemException(SystemStaticValue.REGISTERED_EXCEPTION_CODE,"账号已存在");

        String password = request.getParameter("password");
        checkPassword(password);
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        loginDao.insert(user);
    }

    /**
     *
     * @param request
     * @param response
     * @param email 账号
     * @param password  密码
     * @param isAutoLogin   是不是自动登录调用这个函数的
     * @throws Exception
     */
    public User login(HttpServletRequest request, HttpServletResponse response,String email,String password,boolean isAutoLogin) throws Exception{
        String sql = "select * from t_user where email = ? and password = ?";
        User user = null;

        JdbcTemplate jdbcTemplate = SystemContext.getSystemContext().getJdbcTemplate();
        RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);
        List<User> users = jdbcTemplate.query(sql,userRowMapper,email,password);

        //检测是否有此账号信息
        if(users.size()>0){
            user = users.get(0);
            HttpSession session = request.getSession(true);
            session.setAttribute(USER_OBJ,user);
            //设置session存活时间
            //session.setMaxInactiveInterval(10);

            if(!isAutoLogin){
                //以下为设置cookie
                //cookie设置自动登录标识的值
                String autoLogin = request.getParameter("autoLogin");
                if("true".equals(autoLogin)){
                    //如果勾选了自动登录了，则将账户和密码加密保存在cookie
                    autoLogin = "true";
                    //将user账号进行RSA加密，然后作为key值存放user对象，取出时需要解密
                    String encryptEmail = EncrypRSA.encrypt(user.getEmail());
                    //需要进行URL编码，不然会有非法字符
                    Tool.setCookie(response,USER_EMAIL,URLEncoder.encode(encryptEmail,"UTF-8"),3600*72);
                    //将user密码进行RSA加密，然后作为key值存放user对象，取出时需要解密
                    String encryptPassword = EncrypRSA.encrypt(user.getPassword());
                    //需要进行URL编码，不然会有非法字符
                    Tool.setCookie(response,USER_PASSWORD,URLEncoder.encode(encryptPassword,"UTF-8"),3600*72);
                }else{
                    autoLogin = "false";
                }
                Tool.setCookie(response,AUTOLOGIN,autoLogin,3600*72);
            }
        }else if(!isAutoLogin){
            throw new SystemException(SystemStaticValue.LOGIN_EXCEPTION_CODE,"账号或密码错误");
        }
        return user;
    }

    /**
     * 登出
     * @param request
     * @throws Exception
     */
    public void logout(HttpServletRequest request,HttpServletResponse response){
        Tool.clearCookie(request,response,USER_EMAIL);
        Tool.clearCookie(request,response,USER_PASSWORD);
        Tool.setCookie(response,AUTOLOGIN,"false",3600*72);
        request.getSession().removeAttribute(USER_OBJ);
    }

    public void checkEmail(String email){
        if(Tool.isEmail(email)){
           if(email.length()>40){
               throw new SystemException(SystemStaticValue.EMAIL_EXCEPTION_CODE,"邮箱长度最长40位");
           }
        }else{
            throw new SystemException(SystemStaticValue.EMAIL_EXCEPTION_CODE,"邮箱格式不正确");
        }
    }

    public void checkPassword(String password){
        if(Tool.isLetterDigit(password)){
            if(password.length()<6||password.length()>12){
                throw new SystemException(SystemStaticValue.PASSWORD_EXCEPTION_CODE,"密码只能为数字或字母");
            }

        }else {
            throw new SystemException(SystemStaticValue.PASSWORD_EXCEPTION_CODE,"密码只能为数字或字母");
        }
    }

    /**
     * 检测是否是自动登录用户
     * @param request
     * @param response
     * @throws Exception
     */
    public void checkAutoLogin(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String autoLogin = Tool.getCookie(request,AUTOLOGIN);
        if("true".equals(autoLogin)){
            String encryptEmail = Tool.getCookie(request, USER_EMAIL);
            String encryptPassword = Tool.getCookie(request,USER_PASSWORD);

            try{
                String email = EncrypRSA.decrypt(URLDecoder.decode(encryptEmail,"UTF-8"));
                String password = EncrypRSA.decrypt(URLDecoder.decode(encryptPassword,"UTF-8"));
                login(request,response,email,password,true);
            }catch (Exception e){
                //当服务器重启时，秘钥会跟原先的不同，这里会报错，那么当作用户自动登出
                logout(request,response);
            }
        }
    }
}
