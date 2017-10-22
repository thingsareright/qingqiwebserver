package com.qingqi.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;

/**
 * Created by Administrator on 2017/10/13 0013.
 * 用户的账号和密码的数据表
 */
@Entity
public class User  {
    @Id
    @GeneratedValue
    private Long id;
    private String tel;   //用户的电话号码,是唯一的
    private String password;        //用户的密码

    public User() {
    }

    public User(String tel, String password) {
        this.tel = tel;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
