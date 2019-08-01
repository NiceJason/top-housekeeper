package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/18 8:15
 */
@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

    public void registered(HttpServletRequest request) {
        User user = null;

        String email = request.getParameter("email");
        //检测账号是否已经存在
        user = loginDao.selectByPrimaryKey(email);
        if(user != null)throw new SystemException(SystemStaticValue.REGISTERED_EXCEPTION_CODE,"账号已存在");

        String password = request.getParameter("password");
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        loginDao.insert(user);
    }

    public User login(HttpServletRequest request) {
        String password = request.getParameter("password");
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orCondition("password = ", password);
        User user = loginDao.selectOneByExample(example);
        return user;
    }
}
