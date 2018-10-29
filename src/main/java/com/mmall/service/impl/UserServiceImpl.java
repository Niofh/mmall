package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.pojo.UserExample;
import com.mmall.service.IUserService;
import com.mmall.utli.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            u.setPassword(null);
            return ServerResponse.createBySuccess("登录成功", u);
        }

    }
}
