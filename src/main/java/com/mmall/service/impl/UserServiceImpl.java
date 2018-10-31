package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.pojo.UserExample;
import com.mmall.service.IUserService;
import com.mmall.utli.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/10/29 17:34
 */

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> login(User user) {

        // 条件查询
        UserExample userExample = new UserExample();

        UserExample.Criteria criteria = userExample.createCriteria();

        // 先查询用户名是否正确
        criteria.andUsernameEqualTo(user.getUsername());
        List<User> users = userMapper.selectByExample(userExample);

        if (users.size() == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }


        // md5加密密码验证，查询用户密码是否正确
        criteria.andPasswordEqualTo(MD5Util.MD5EncodeUtf8(user.getPassword()));
        users = userMapper.selectByExample(userExample);

        if (users.size() == 0) {
            return ServerResponse.createByErrorMessage("用户密码错误");
        } else {
            User u = users.get(0);
            return ServerResponse.createBySuccess("登录成功", u);
        }

    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> addUser(User user) {

        // 判断用户名是否存在
        ServerResponse<String> userCheckValid = this.checkValid(user.getUsername(), Const.USER_NAME);
        if (!userCheckValid.isSuccess()) {
            return userCheckValid;
        }

        // 判断邮件名是否存在
        ServerResponse<String> emailCheckValid = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!emailCheckValid.isSuccess()) {
            return emailCheckValid;
        }

        // 添加角色
        user.setRole(Const.Roles.ROLE_CUSTOMER);
        // 密码MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int insert = userMapper.insertSelective(user);
        if (insert == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }


    /**
     * 校验用户名和邮箱是否存在
     *
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNoneBlank(type)) {
            // 条件查询
            UserExample userExample = new UserExample();
            UserExample.Criteria criteria = userExample.createCriteria();

            if (type.equals(Const.USER_NAME)) {             // 判断用户名是否存在

                criteria.andUsernameEqualTo(str);
                List<User> userList = userMapper.selectByExample(userExample);
                if (userList.size() > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }

            } else if (type.equals(Const.EMAIL)) {       // 判断邮件名是否存在

                criteria.andEmailEqualTo(str);

                List<User> userList = userMapper.selectByExample(userExample);
                if (userList.size() > 0) {
                    return ServerResponse.createByErrorMessage("邮件名已存在");
                }
            }

        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }


        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 根据用户名称获取问题
     *
     * @param username
     * @return
     */
    @Override
    public ServerResponse<String> getQuestionByUserName(String username) {

        ServerResponse<String> checkValid = this.checkValid(username, Const.USER_NAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<User> userList = userMapper.selectByExample(userExample);

        User user = userList.get(0);
        String question = user.getQuestion();

        if (StringUtils.isBlank(question)) {
            return ServerResponse.createByErrorMessage("用户没有设置问题");
        }

        return ServerResponse.createBySuccess(question);
    }

    /**
     * 验证问题
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {

        // SQL查询
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andQuestionEqualTo(question);
        criteria.andAnswerEqualTo(answer);

        List<User> userList = userMapper.selectByExample(userExample);

        if (userList.size() > 0) {

            String forgetToken = UUID.randomUUID().toString();
            // 把需要修改密码用户生成token来修改密码
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);

            return ServerResponse.createBySuccess("验证成功", forgetToken);
        }


        return ServerResponse.createByErrorMessage("问题的答案错误");
    }


    @Override
    public ServerResponse<String> uodateForgetPassword(String username, String password, String token) {

        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token不能为空");
        }

        if (StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorMessage("新密码不能为空");
        }

        ServerResponse<String> checkValid = this.checkValid(username, Const.USER_NAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String tokenCache = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);

        if (StringUtils.isBlank(tokenCache)) {
            return ServerResponse.createByErrorMessage("token已经过期，请重新获取");
        }

        if (!StringUtils.equals(token, tokenCache)) {

            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");

        }

        // 加密密码
        String md5pwd = MD5Util.MD5EncodeUtf8(password);
        User user = new User();
        user.setPassword(md5pwd);

        // 根据用户名称修改密码
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);

        // 更新用户密码
        int i = userMapper.updateByExampleSelective(user, userExample);

        if (i == 0) {
            return ServerResponse.createByErrorMessage("修改密码失败");
        }

        return ServerResponse.createBySuccessMessage("修改密码成功");
    }

    /**
     * 修改密码
     *
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> updateResetPassword(String passwordOld, String passwordNew, User user) {

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andPasswordEqualTo(MD5Util.MD5EncodeUtf8(passwordOld));
        List<User> userList = userMapper.selectByExample(userExample);

        if (userList.size() == 0) {
            return ServerResponse.createByErrorMessage("用户旧密码错误");
        }

        // 更新密码
        User u = new User();
        u.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        u.setId(user.getId());
        int i = userMapper.updateByPrimaryKeySelective(u);

        if (i == 0) {
            return ServerResponse.createByErrorMessage("修改密码失败");
        }
        return ServerResponse.createBySuccessMessage("修改密码成功");
    }

    /**
     * 通过id查询用户信息
     *
     * @param id
     * @return
     */
    @Override
    public ServerResponse<User> getInformationById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return ServerResponse.createByError("查询失败", user);
        }
        return ServerResponse.createBySuccess("查询成功", user);
    }


    /**
     * 通过id更新用户信息
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> updateInformationById(User user) {
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i == 0) {
            return ServerResponse.createByError("用户更新失败", user);
        }
        return ServerResponse.createBySuccess("用户更新成功", user);
    }
}
