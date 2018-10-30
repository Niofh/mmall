package com.mmall.common;

/**
 * 全局自定义变量
 *
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/10/30 9:32
 */
public class Const {
    public final static String CURRENT_USER = "CURRENT_USER"; // 当前用户
    public final static String USER_NAME = "username"; // 用户名属性
    public final static String EMAIL = "email"; // 邮件名属性


    public interface Roles {
        int ROLE_CUSTOMER = 0; // 普通用户
        int ROLE_ADMIN = 1; // 管理员
    }
}
