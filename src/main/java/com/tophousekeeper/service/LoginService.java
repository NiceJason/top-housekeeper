package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.User;
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
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = new User();
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
