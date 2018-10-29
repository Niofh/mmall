package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/10/29 17:32
 */
public interface IUserService {


    /**
     * 登录
     * @param user
     * @return
     */
    ServerResponse<User> login(User user);


}
