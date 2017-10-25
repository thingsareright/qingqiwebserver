package com.qingqi.controller;

import com.qingqi.entity.User;
import com.qingqi.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/13 0013.
 * 这个控制器用于处理关于User表的数据请求（增删改查）
 */
@RestController
@RequestMapping("/user")    //这里主要是为了与管理员账户分开，所以不用"/"
public class UserDataController {

    @Autowired
    public UserRepository userRepository;


    //required=false 表示url中可以不穿入id参数，此时就使用默认参数
    /**
     * 这个方法主要是首次注册使用
     * 返回1表示这次是首次注册成功，返回0表示首次注册失败
     * 请求格式: http://localhost:8080/user/register?tel=18838951998&password=zhang
     */
    @RequestMapping(value="/register")
    public String register(@RequestParam(value = "tel",required = false, defaultValue = "0") String tel,
                            @RequestParam(value = "password", required = false, defaultValue = "0") String password){

        if (tel.equals("0") || tel.equals("0"))
            return "0";
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAllByTel(tel);
        if (userList.size() == 0){
            //检测到账号未注册，进行逻辑处理
            User user = new User(tel,password);
            userRepository.save(user);
            return "1";
        }
        return "0";
    }

    /**
     * 这个方法主要是登录检查使用
     * 返回1表示这次是登录成功，返回0表示登录失败
     * http://localhost:8080/user/login?tel=18838951998&password=zhang
     */
    @RequestMapping(value="/login")
    public String login(@RequestParam(value = "tel",required = false, defaultValue = "0") String tel,
                           @RequestParam(value = "password", required = false, defaultValue = "0") String password){

        if (tel.equals("0") || tel.equals("0"))
            return new String("0");
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAllByTelAndPassword(tel, password);
        if (userList.size() > 0){
            return new String("1");
        }
        return "0";
    }


    /**
     * 下面这个接口是用来在已知原来密码的情况下修改密码，这个接口在本地的请求格式是：
     * http://localhost:8080/user/changepassword?tel=18838951998&password=zhang&newpassword=123
     * 成功则返回1，否则返回0
     */
    @RequestMapping("/changepassword")
    public String changePassword(@RequestParam(value = "tel", required = false, defaultValue = "00000000000") String tel,
                                  @RequestParam(value = "password", required = false, defaultValue = "0000")String password,
                                  @RequestParam(value = "newpassword", required = false, defaultValue = "0000")String newpassword) {
        if (tel.equals("00000000000") || password.equals("0000") || newpassword.equals("0000"))
            return "0";
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAllByTelAndPassword(tel, password);
        if (userList.size() > 0){
            User user = userList.get(0);
            int flag = userRepository.updateUser(user.getTel(),newpassword);
            if (flag == 0)
                return "0";
            return "1";
        }
        return "0";
    }

    /**
     * 下面这个接口是用来在已知原来密码的情况下修改密码，这个接口在本地的请求格式是：
     * http://localhost:8080/user/forgetpassword?tel=18838951998&password=zhang
     * 成功则返回1，否则返回0
     */
    @RequestMapping("/forgetpassword")
    public String changePassword(@RequestParam(value = "tel", required = false, defaultValue = "00000000000") String tel,
                                 @RequestParam(value = "password", required = false, defaultValue = "0000")String password) {
        if (tel.equals("00000000000") || password.equals("0000") )
            return "0";
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAllByTel(tel);
        if (userList.size() > 0){
            User user = userList.get(0);
            int flag = userRepository.updateUser(user.getTel(),password);
            if (flag == 0)
                return "0";
            System.out.println("*************1");
            return "1";
        }
        System.out.println("*************2");
        return "0";
    }



}
