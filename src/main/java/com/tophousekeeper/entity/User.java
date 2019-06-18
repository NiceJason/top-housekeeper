package com.tophousekeeper.entity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/6/13 8:25
 */
@Table(name = "t_user")
public class User {
    @Id
    private String email;
    private String password;

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
