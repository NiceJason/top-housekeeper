package com.tophousekeeper.service;

import com.tophousekeeper.dao.function.LoginDao;
import com.tophousekeeper.entity.User;
import com.tophousekeeper.system.SystemContext;
import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public User login(HttpServletRequest request) {
        String sql = "select * from t_user where email = ? and password = ?";
        User user = null;
        String email = request.getParameter("email");
        checkEmail(email);
        String password = request.getParameter("password");
        checkPassword(password);

        try(Connection connection = SystemContext.getSystemContext().getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1,email);
                preparedStatement.setString(2,password);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()){
                        user = new User(email,password);
                    }else {
                        throw new SystemException(SystemStaticValue.LOGIN_EXCEPTION_CODE,"账号或密码错误");
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemException(SystemStaticValue.DATASOURCE_EXCEPTION,"数据库错误");
        }
        return user;
    }

    private void checkEmail(String email){
        if(Tool.isEmail(email)){
           if(email.length()>40){
               throw new SystemException(SystemStaticValue.EMAIL_EXCEPTION_CODE,"邮箱长度最长40位");
           }
        }else{
            throw new SystemException(SystemStaticValue.EMAIL_EXCEPTION_CODE,"邮箱格式不正确");
        }
    }

    private void checkPassword(String password){
        if(Tool.isLetterDigit(password)){
            if(password.length()<6||password.length()>12){
                throw new SystemException(SystemStaticValue.PASSWORD_EXCEPTION_CODE,"密码只能为数字或字母");
            }

        }else {
            throw new SystemException(SystemStaticValue.PASSWORD_EXCEPTION_CODE,"密码只能为数字或字母");
        }
    }
}
