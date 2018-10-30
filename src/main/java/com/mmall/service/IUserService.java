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
     *
     * @param user
     * @return
     */
    ServerResponse<User> login(User user);


    /**
     * 注册
     *
     * @param user
     * @return
     */
    ServerResponse<String> addUser(User user);

    /**
     * 校验参数
     *
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 根据用户名称获取问题
     *
     * @param username
     * @return
     */
    ServerResponse<String> getQuestionByUserName(String username);


    /**
     * 验证问题
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);


    /**
     * （忘记密码功能）根据生成token来修改密码
     *
     * @param username
     * @param password
     * @param token    生成token
     * @return
     */
    ServerResponse<String> uodateForgetPassword(String username, String password, String token);


    /**
     * 修改密码
     *
     * @param passwordOld
     * @param passwordNew
     * @param user        用户信息
     * @return
     */
    ServerResponse<String> updateResetPassword(String passwordOld, String passwordNew, User user);

    /**
     * 通过id查询用户信息
     * @param id
     * @return
     */
    ServerResponse<User> getInformationById(Integer id);


    /**
     * 通过id更新用户信息
     * @param user
     * @return
     */
    ServerResponse<User> updateInformationById(User user);


}
