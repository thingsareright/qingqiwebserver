package com.qingqi.utils;

/**
 * Created by Administrator on 2017/10/14 0014.
 * 这个是后台配置的重要文件，需要高度保密，否则系统安全将会受到严重威胁
 */
public class ConstantSecret {
    private static final String token = "afhjjfl589451";    //管理员身份的唯一标识

    public static String getToken() {
        return token;
    }

}
