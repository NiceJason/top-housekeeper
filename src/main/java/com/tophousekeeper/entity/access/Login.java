package com.tophousekeeper.entity.access;

import javax.validation.constraints.NotEmpty;

/**
 * @auther: NiceBin
 * @description: 登录对象
 * @date: 2020/4/22 17:14
 */
public class Login {
    //账号
    @NotEmpty(message = "账号不能为空")
    private String email;
    //密码
    @NotEmpty(message = "密码不能为空")
    private String password;
    //是否自动登录
    private String autoLogin;

    public Login() {
    }

    public Login(String email, String password, String autoLogin) {
        this.email = email;
        this.password = password;
        this.autoLogin = autoLogin;
    }

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

    public String getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(String autoLogin) {
        this.autoLogin = autoLogin;
    }
}
