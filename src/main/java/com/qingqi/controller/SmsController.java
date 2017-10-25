package com.qingqi.controller;

import com.github.qcloudsms.*;
import com.qingqi.utils.CheckInputUtils;
import com.qingqi.utils.ConstantSecret;
import com.qingqi.utils.SmsUtil;
import com.qingqi.utils.StrUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/10/24 0024.
 * 这个控制类主要用于提供短信接口，以便于安卓手机服务端发送短信
 */
@RestController
@RequestMapping("/sms")
public class SmsController {


    


    /**
     * 下面这个请求主要是发送取货成功的通知短信，传过来四个参数，第一个参数是验证口令，第二个是手机号，其余
     * 两个参数是腾讯云短信取货成功短信模板的两个参数
     * 请求格式（以本地服务器为例）：http://localhost:8080/sms/sendgetpackage?token=111&tel=18838951998&param1=111111111&param2=孟
     * 发送成功返回1，不成功返回0
     */
    @RequestMapping("/sendgetpackage")
    public String sendgetpackage(@RequestParam(value = "token", required = false,defaultValue = "0") String token,
                                 @RequestParam(value = "tel", required = false,defaultValue = "0") String tel,
                                 @RequestParam(value = "param1", required = false,defaultValue = "0")String param1,
                                 @RequestParam(value = "param2", required = false,defaultValue = "0")String param2) {

        if (!token.equals(ConstantSecret.getToken())) {
            return "0";
        }
        try {
            String result = SmsUtil.sendgetpackage(tel, param1, param2);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 下面这个请求主要是发送正在配送的通知短信，传过来四个参数，第一个参数是验证口令，第二个是手机号，其余
     * 两个参数是腾讯云短信配送中状态短信模板的两个参数
     * 请求格式（以本地服务器为例）：http://localhost:8080/sms/senddispatching?token=111&tel=18838951998&param1=111111111&param2=孟
     * 发送成功返回1，不成功返回0
     */
    @RequestMapping("/senddispatching")
    public String senddispatching(@RequestParam(value = "token", required = false,defaultValue = "0") String token,
                                  @RequestParam(value = "tel", required = false,defaultValue = "0") String tel,
                                 @RequestParam(value = "param1", required = false,defaultValue = "0")String param1,
                                 @RequestParam(value = "param2", required = false,defaultValue = "0")String param2) {
        if (!token.equals(ConstantSecret.getToken())) {
            return "0";
        }

        try {
            String result = SmsUtil.senddispatching(tel, param1, param2);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }





    /**
     * 下面这个请求主要是发送取货成功的通知短信，传过来四个参数，第一个参数是验证口令，第二个是手机号，其余
     * 两个参数是腾讯云短信取货成功短信模板的两个参数
     * 请求格式（以本地服务器为例）：http://localhost:8080/sms/sendungetpackage?token=111&tel=18838951998&param1=111111111&param2=孟
     * 发送成功返回1，不成功返回0
     */
    @RequestMapping("/sendungetpackage")
    public String sendungetpackage(@RequestParam(value = "token", required = false,defaultValue = "0") String token,
                                   @RequestParam(value = "tel", required = false,defaultValue = "0") String tel,
                                   @RequestParam(value = "param1", required = false,defaultValue = "0")String param1,
                                   @RequestParam(value = "param2", required = false,defaultValue = "0")String param2) {
        if (!token.equals(ConstantSecret.getToken())) {
            return "0";
        }

        try {
            String result = SmsUtil.sendungetpackage(tel, param1, param2);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


    /**
     * 下面这个路径用于发送验证码短信，并向客户端返回验证码，验证码是六位随机数字符串
     * 但是如果发送失败，则返回"0"
     * 请求路径格式： http://localhost:8080/sms/getCode?tel=18838951998
     */
    @RequestMapping("/getCode")
    public String getCode(@RequestParam(value = "tel", required = false, defaultValue = "0") String tel){
        //首先检查手机号是否符合我们的要求
        if (!CheckInputUtils.checkTel(tel)) {
            return "0";
        }
        //首先生成一个由六位数字组成的随机字符串
        String code = StrUtil.getRandomCode();
        try {
            String result = SmsUtil.sendgetCode(tel, code, "5");
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

}
