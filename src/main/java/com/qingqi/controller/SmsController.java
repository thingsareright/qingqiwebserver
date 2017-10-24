package com.qingqi.controller;

import com.github.qcloudsms.*;
import com.qingqi.utils.SmsUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/24 0024.
 * 这个控制类主要用于提供短信接口，以便于安卓手机服务端发送短信
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    /**
     * 下面这个请求主要是发送取货成功的通知短信，传过来三个参数，第一个参数是手机号，其余
     * 两个参数是腾讯云短信取货成功短信模板的两个参数
     * 请求格式（以本地服务器为例）：http://localhost:8080/sms/sendgetpackage?tel=18838951998&param1=111111111&param2=孟
     * 发送成功返回1，不成功返回0
     */
    @RequestMapping("/sendgetpackage")
    public String sendgetpackage(@RequestParam(value = "tel", required = false,defaultValue = "0") String tel,
                                 @RequestParam(value = "param1", required = false,defaultValue = "0")String param1,
                                 @RequestParam(value = "param2", required = false,defaultValue = "0")String param2) {

        try {
            String result = SmsUtil.sendSms(tel, param1, param2);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 下面这个请求主要是发送正在配送的通知短信，传过来三个参数，第一个参数是手机号，其余
     * 两个参数是腾讯云短信配送中状态短信模板的两个参数
     * 请求格式（以本地服务器为例）：http://localhost:8080/sms/senddispatching?tel=18838951998&param1=111111111&param2=孟
     * 发送成功返回1，不成功返回0
     */
    @RequestMapping("/senddispatching")
    public String senddispatching(@RequestParam(value = "tel", required = false,defaultValue = "0") String tel,
                                 @RequestParam(value = "param1", required = false,defaultValue = "0")String param1,
                                 @RequestParam(value = "param2", required = false,defaultValue = "0")String param2) {

        try {
            String result = SmsUtil.sendSms(tel, param1, param2);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }





    /**
     * 下面这个请求主要是发送取货成功的通知短信，传过来三个参数，第一个参数是手机号，其余
     * 两个参数是腾讯云短信取货成功短信模板的两个参数
     * 请求格式（以本地服务器为例）：http://localhost:8080/sms/sendungetpackage?tel=18838951998&param1=111111111&param2=孟
     * 发送成功返回1，不成功返回0
     */
    @RequestMapping("/sendungetpackage")
    public String sendungetpackage(@RequestParam(value = "tel", required = false,defaultValue = "0") String tel,
                                   @RequestParam(value = "param1", required = false,defaultValue = "0")String param1,
                                   @RequestParam(value = "param2", required = false,defaultValue = "0")String param2) {

        try {
            String result = SmsUtil.sendSms(tel, param1, param2);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


}
