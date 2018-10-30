package com.mmall.common;

/**
 * Created by Nicofh
 * 一些返回客户端code编码
 */
public enum ResponseCode {

    SUCCESS(0,"SUCCESS"), // 请求成功
    ERROR(1,"ERROR"), // 请求失败
    NEED_LOGIN(10,"NEED_LOGIN"), // 没有登录状态，需要强制登录
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;


    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }

}
