package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.entity.access.Login;
import com.tophousekeeper.entity.access.RegisterInfo;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.running.SystemContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.servlet.http.HttpServletResponse;

/**
 * @auther: NiceBin
 * @description:    用户登录服务，包含登录，注册，自动登录等功能
 *                  采用jwt生成token进行登录
 *                  如果缓存里没该登录信息了，则要求重新登录
 * @date: 2020/5/8 21:07
 */
public class NewLoginService {

    @Autowired
    private LoginDao loginDao;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 注册
     * @param registerInfo
     */
    public void registered(RegisterInfo registerInfo) {
        User user = null;
        String email = registerInfo.getEmail();
        //检测账号是否已经存在
        user = loginDao.selectByPrimaryKey(email);
        if(user != null)throw new SystemException(SystemStaticValue.REGISTERED_EXCEPTION_CODE,"账号已存在");

        String password = registerInfo.getPassword();
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        loginDao.insert(user);
    }

    /**
     * 登录
     * 将登录后的User存入Redis缓存，为以后使用
     * 在cookie上设置同等时效的token
     * @param login
     * @param response
     * @return
     */
    User login(Login login, HttpServletResponse response){
        String sql = "select * from t_user where email = ? and password = ?";

        JdbcTemplate jdbcTemplate = SystemContext.getSystemContext().getJdbcTemplate();
        RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);
        User user = jdbcTemplate.queryForObject(sql, userRowMapper,login.getEmail(),login.getPassword());
        if(user!=null){

        }
        return null;
    }

    /**
     * 通过Token实现自动登录
     * 如果Token或者对应的User在缓存已经失效的话，需要重新登录
     * @param token
     * @return
     */
    User loginByToken(String token){
        return null;
    }
}
