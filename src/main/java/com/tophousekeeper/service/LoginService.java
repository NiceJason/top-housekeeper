package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @author NiceBin
 * @description: 登录注册模块
 * @date 2019/6/18 8:15
 */
@Service
public class LoginService {

       @Autowired
       private LoginDao loginDao;

       public void registered(User user){
           loginDao.insert(user);
       }


       public User login(User checkUser){
           Example example = new Example(User.class);
           Example.Criteria criteria=example.createCriteria();
           criteria.orCondition("password = ",checkUser.getPassword());
           User user =loginDao.selectOneByExample(example);
           return user;
       }
}
