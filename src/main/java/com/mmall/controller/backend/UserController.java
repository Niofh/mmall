package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/10/30 17:14
 */
@RequestMapping("/manage/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse<User> login(User user, HttpSession httpSession) {
        ServerResponse<User> userServerResponse = iUserService.login(user);
        if (!userServerResponse.isSuccess()) {
            return userServerResponse;
        }
        Integer role = userServerResponse.getData().getRole();

        if (role.equals(Const.Roles.ROLE_ADMIN)) {
            //说明登录的是管理员
            httpSession.setAttribute(Const.CURRENT_USER, user);
            return userServerResponse;
        }
        return ServerResponse.createByErrorMessage("你不是管理员");
    }
}
