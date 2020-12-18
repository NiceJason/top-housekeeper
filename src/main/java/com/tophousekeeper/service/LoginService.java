package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.entity.access.Login;
import com.tophousekeeper.entity.access.RegisterInfo;
import com.tophousekeeper.system.CommonStaticValue;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.running.SystemContext;
import com.tophousekeeper.system.security.EncrypRSA;
import com.tophousekeeper.util.JwtTool;
import com.tophousekeeper.util.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
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

    //账户
    public final static String USER_EMAIL = "userEmail";
    //密码
    public final static String USER_PASSWORD = "userPassword";
    //用来设置cookie，自动登录标记，主要是自动登录的时候用
    public final static String AUTOLOGIN = "autoLogin";
    //session存放user对象
    public final static String USER_OBJ = "userObj";

    @Transactional
    public void registered(RegisterInfo registerInfo) {
        User user = null;
        String email = registerInfo.getEmail();
        //检测账号是否已经存在
        user = loginDao.selectByPrimaryKey(email);
        if(user != null)throw new SystemException(SystemStaticValue.REGISTERED_EXCEPTION_CODE,"账号已存在");

        String password = registerInfo.getPassword();

        //检测账号，密码
        checkEmail(email);
        checkPassword(password);

        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        loginDao.insert(user);
    }

    /**
     * 用户登录，分为自动登录（会被拦截调用本函数）和正常登录
     * 登录成功后，会往Cookie放入新建的Token
     * 会往session放置用户对象，key =USER_OBJ
     * 如果勾选了自动登录，会往Cookie放置相关信息
     * @param login
     * @param isAutoLogin  是不是自动登录调用这个函数的
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public User login(Login login, boolean isAutoLogin, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sql = "select * from t_user where email = ? and password = ?";
        User user = null;

        JdbcTemplate jdbcTemplate = SystemContext.getSystemContext().getJdbcTemplate();
        RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);
        List<User> users = jdbcTemplate.query(sql,userRowMapper,login.getEmail(),login.getPassword());

        //检测是否有此账号信息
        if(users.size()>0){
            user = users.get(0);
            HttpSession session = request.getSession(true);
            session.setAttribute(USER_OBJ,user);
            //设置session存活时间
            session.setMaxInactiveInterval((int)SystemStaticValue.SESSION_LIVE_TIME);

            //设置Token
            HashMap<String,String> map = new HashMap<>();
            map.put(USER_EMAIL,login.getEmail());
            String token = JwtTool.createToken(JwtTool.SUBJECT_ONLINE_STATE,SystemStaticValue.TOKEN_LIVE_TIME,map);
            Tool.setCookie(response, CommonStaticValue.TOKEN,token);

            if(!isAutoLogin){
                //以下为设置cookie
                //cookie设置自动登录标识的值
                String autoLogin = login.getAutoLogin();
                if("true".equals(autoLogin)){
                    //如果勾选了自动登录了，则将账户和密码加密保存在cookie
                    autoLogin = "true";
                    //将user账号进行RSA加密，然后作为key值存放user对象，取出时需要解密
                    String encryptEmail = EncrypRSA.encrypt(user.getEmail());
                    //需要进行URL编码，不然会有非法字符
                    Tool.setCookie(response,USER_EMAIL, URLEncoder.encode(encryptEmail,"UTF-8"));
                    //将user密码进行RSA加密，然后作为key值存放user对象，取出时需要解密
                    String encryptPassword = EncrypRSA.encrypt(user.getPassword());
                    //需要进行URL编码，不然会有非法字符
                    Tool.setCookie(response,USER_PASSWORD,URLEncoder.encode(encryptPassword,"UTF-8"));
                }else{
                    autoLogin = "false";
                }
                Tool.setCookie(response,AUTOLOGIN,autoLogin);
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
        Tool.clearCookie(request,response,CommonStaticValue.TOKEN);
        Tool.setCookie(response,AUTOLOGIN,"false");
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
                throw new SystemException(SystemStaticValue.PASSWORD_EXCEPTION_CODE,"密码长度需在6-12位");
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

                Login login = new Login(email,password,autoLogin);

                login(login,true,request,response);
            }catch (Exception e){
                //当服务器重启时，秘钥会跟原先的不同，这里会报错，那么当作用户自动登出
                logout(request,response);
            }
        }
    }

    /**
     * 从Session中获取登录状态的User
     * @param request
     * @return 如果有则返回User，无则为null
     */
    public User getAliveUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            Object user = session.getAttribute(USER_OBJ);
            if(user != null){
                return (User)user;
            }
        }
        return null;
    }
}
