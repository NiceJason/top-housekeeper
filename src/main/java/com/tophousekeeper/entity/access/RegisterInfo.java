package com.tophousekeeper.entity.access;

import com.tophousekeeper.entity.access.valid_check.RegisterCheck;

/**
 * @auther: NiceBin
 * @description: 系统的注册对象
 * @date: 2020/4/21 20:50
 */
public class RegisterInfo {
    //账号
    @RegisterCheck(groups = RegisterCheck.EmailCheckValidator.class)
    private String email;
    //密码
    @RegisterCheck(groups = RegisterCheck.PasswordCheckValidator.class)
    private String password;

    //-------------------------------以下为set和get
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
