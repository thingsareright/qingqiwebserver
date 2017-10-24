package com.qingqi.controller;

import com.qingqi.entity.EI;
import com.qingqi.entity.EIRepository;
import com.qingqi.entity.User;
import com.qingqi.entity.UserRepository;
import com.qingqi.utils.ConstantSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/14 0014.
 * 这个类中封装了关于EI表的数据请求接口
 * 主要包括：
 * 1. 返回某一用户对应的所有EI表的数据
 * 2. 清除某一轻骑未接单的数据
 * 3. 返回处于某一状态下的所有EI表的数据
 * 4. 将某一条记录的状态由未领转变为其他两种状态之一
 */
@RestController
@RequestMapping("/ei")
public class EIDataController {
    @Autowired
    private EIRepository eiRepository;
    @Autowired
    private UserRepository userRepository;  //还要用这个实体类做一些必要的验证

    /**
     * 返回某一用户对应的所有EI表的数据
     * 请求格式（访问本机服务器的时候）：http://localhost:8080/ei/getEIofOneUser?tel=18838951998&password=zhang
     */
    @RequestMapping(value = "/getEIofOneUser")
    public List<EI> getEIofOneUser(@RequestParam(value = "tel", required = false, defaultValue = "00000000000")String tel,
                                    @RequestParam(value = "password", required = false,defaultValue = "000000")String password){

        //判断传入是否为空
        if (tel.equals("00000000000") || password.equals("000000"))
            return new ArrayList<EI>();

        //先判断用户表里有没有这个用户
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAllByTelAndPassword(tel, password);


        List<EI> eiList = new ArrayList<>();
        //如果有，取出其userid
        if (userList.size() > 0){
            Long userid = (userList.get(0).getId());
            eiList = eiRepository.withUseridQuery(userid);
            return eiList;
        }
        return eiList;
    }

    /**
     * 清除某一轻骑未接单的数据
     * 接口请求格式（还是以本地服务器访问为例说明）：
     * http://localhost:8080/ei/deleteOneEIData?id=1&tel=18838951998&password=zhang
     * 成功则返回1，否则返回0
     */
    @RequestMapping("/deleteOneEIData")
    public Long deleteOneEIData(@RequestParam(value = "id", required = false, defaultValue = "0")Long id,
                                @RequestParam(value = "tel", required = false, defaultValue = "00000000000")String tel,
                                @RequestParam(value = "password", required = false,defaultValue = "000000")String password) {
        //判断传入是否为空
        if (tel.equals("00000000000") || password.equals("000000") || id.equals("0"))
            return new Long(0);

        //先判断用户表里有没有账户名和密码与这个用户相同的记录
        List<User> userList = new ArrayList<>();
        userList = userRepository.findAllByTelAndPassword(tel, password);
        //没有这样的用户的话就返回失败状态码
        if (userList.size() == 0)
            return new Long(0);
        Long userid = userList.get(0).getId();


        List<EI> eiList = eiRepository.findAllByIdAndUserid(id, userid);
        //如果没有这样的EI记录的话也按失败处理
        if (eiList.size() == 0)
            return new Long(0);
        eiRepository.deleteOrderById(id, userid);
        return new Long(1);
    }

    /**
     * 返回处于某快递点且在某一状态下的所有EI表的数据
     * 接口格式（仍以本地服务器为例）：http://localhost:8080/ei/getEIofOneState?token=afhjjfl589451&state=1&smsaddress=2
     * state参数意义：0表示轻骑看到通知，1表示轻骑已经取到货，2表示轻骑没有取到货
     * TODO 注意token的值是唯一的标识，需要高度保密
     */
    @RequestMapping("/getEIofOneState")
    public List<EI> getEIofOneState(@RequestParam(value = "token", required = false, defaultValue = "111")String token,
                                     @RequestParam(value = "state", required = false, defaultValue = "999") Long state,
                                             @RequestParam(value = "smsaddress", required = false, defaultValue = "999")Long smsaddress){
        if (!token.equals(ConstantSecret.getToken()) || state.equals("999") || smsaddress.equals("999")){
            return new ArrayList<EI>();
        }
        List<EI> eiList = new ArrayList<>();
        eiList = eiRepository.findAllByStateAndSmsaddress(state,smsaddress);
        return eiList;
    }

    /**
     * 将某一条记录的状态由未领转变为其他两种状态之一
     * 接口格式（仍以本地服务器为例）：http://localhost:8080/ei/setEIofOneState?token=afhjjfl589451&id=1&state=1
     * state参数意义：0表示轻骑未接单，1表示轻骑接单且在中午送货，2表示轻骑接单且在下午送货
     */
    @RequestMapping("/setEIofOneState")
    public Long setEIofOneState(@RequestParam(value = "token", required = false, defaultValue = "0")String token,
                                    @RequestParam(value = "id", required = false, defaultValue = "0")Long id,
                                    @RequestParam(value = "state", required = false, defaultValue = "999")Long state){
        if (!token.equals(ConstantSecret.getToken()) || state.equals("999") || id.equals("0")){
            return new Long(0);
        }
        eiRepository.updateStateById(id, state);
        /**
         * 根据轻骑选择的状态来帮他们通过系统发送短信通知用户，这个接口以后还要加个物流单号参数来充当短信正文内容
         */
        return new Long(1);
    }


    /**
     * 用户向轻骑存放数据的接口
     * 接口格式（仍以本地服务器为例）：http://localhost:8080/ei/saveData?&usertel=18838951998&password=zhang&awb=111&tel=18838951998&sms=111&address=柳园21号楼&smsaddress=5&name=me
     * 返回0表示添加成功，返回1表示添加失败
     private Long state;          //这条物流的状态：0表示轻骑看到通知，1表示轻骑已经取到货，2表示轻骑没有取到货
                     userid
     * 这里的参数我来说明一下几点内容：
     * 1.usertel和password是用来做登录验证的，正确的话用户才能更改权限
     * 2.tel是用户的订单中的电话号码，这与用户注册的号码可能会不一样
     * 3.address是用户自己的收货地址
     * 4.smsaddress是用户对自己快递现在存放的位置的预选：包括菊二，菊五种种
     */
    @RequestMapping("/saveData")
    public Long saveEIData(@RequestParam(value = "usertel", required = false, defaultValue = "0")  String usertel,
                           @RequestParam(value = "password", required = false, defaultValue = "0")   String password,
                               @RequestParam(value = "awb", required = false, defaultValue = "0")   String awb,
                               @RequestParam(value = "tel", required = false, defaultValue = "0")   String tel,
                               @RequestParam(value = "sms", required = false, defaultValue = "0")   String sms,
                               @RequestParam(value = "address", required = false, defaultValue = "0")     String address,
                               @RequestParam(value = "smsaddress", required = false, defaultValue = "0")  Long smsaddress,
                           @RequestParam(value = "name", required = false, defaultValue = "0")  String name
                               ) {
        //第一步如果上面的值是默认值，那么直接返回，不对数据库进行操作
        if (usertel.equals("0") || password.equals("0") || awb.equals("0") || tel.equals("0") || sms.equals("0")
                || address.equals("0") || smsaddress.equals("0") || name.equals("0"))
            return new Long(0);

        //第二步如果数据库中找不到对应的用户，返回0
        List<User> userList = userRepository.findAllByTelAndPassword(usertel, password);
        if (userList.size() == 0)
            return new Long(0);

        //第三步，获取用户userid
        Long userid = userList.get(0).getId();

        //第四步，将数据存入数据库
        EI ei = new EI(awb, tel, sms, address, new Long(0),new Long(userid), new Long(smsaddress), name);
        eiRepository.save(ei);
        return new Long(1);
    }

    /**
     * 获取某一id对应的EI记录
     * 接口格式（仍以本地服务器为例）：http://localhost:8080/ei/getSingleEIDataById?&usertel=18838951998&password=zhang&id=1
     * */
    @RequestMapping("/getSingleEIDataById")
    public EI getSingleEIDataById(@RequestParam(value = "usertel", required = false, defaultValue = "0")  String usertel,
                                      @RequestParam(value = "password", required = false, defaultValue = "0")   String password,
                                      @RequestParam(value = "id", required = false, defaultValue = "-1")   Long id){
        //先判断参数是否足够
        if (usertel.equals("0") || password.equals("0") || id.longValue() == -1){
            return new EI();
        }
        //取出对应的数据
        EI ei = new EI();
        ei = eiRepository.findById(id);
        if (ei == null)     //以防万一
            return new EI();
        return ei;
    }
}
