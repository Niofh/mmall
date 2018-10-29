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
    public ServerResponse<User> login(User user) {

        // 条件查询
        UserExample userExample = new UserExample();

        UserExample.Criteria criteria = userExample.createCriteria();

        criteria.andUsernameEqualTo(user.getUsername());
        List<User> users = userMapper.selectByExample(userExample);
        User u = users.get(0);

        if (u == null) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }


        // todo md5加密密码验证
        criteria.andPasswordEqualTo(MD5Util.MD5EncodeUtf8(user.getPassword()));
        users = userMapper.selectByExample(userExample);

        u = users.get(0);

        if (u == null) {
            return ServerResponse.createByErrorMessage("用户密码错误");
        }

        return ServerResponse.createBySuccess("登录成功", u);
    }
}
